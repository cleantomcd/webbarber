package com.webbarber.webbarber.service;

import com.webbarber.webbarber.dto.BookingDTO;
import com.webbarber.webbarber.dto.BookingInfoDTO;
import com.webbarber.webbarber.dto.RequestBookingDTO;
import com.webbarber.webbarber.entity.Booking;
import com.webbarber.webbarber.exception.BookingNotFoundException;
import com.webbarber.webbarber.exception.ServiceNotFoundException;
import com.webbarber.webbarber.exception.TimeSlotNotAvailableException;
import com.webbarber.webbarber.exception.UserNotFoundException;
import com.webbarber.webbarber.repository.BookingRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável por gerenciar agendamentos de serviços de barbeiro.
 * Oferece métodos para criar, validar, consultar e cancelar agendamentos.
 */
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ServiceService serviceService;
    private final TimeSlotAvailabilityService timeSlotAvailabilityService;

    /**
     * Construtor para inicializar o serviço com as dependências necessárias.
     *
     * @param bookingRepository Repositório para manipulação dos agendamentos.
     * @param userService Serviço relacionado aos usuários.
     * @param serviceService Serviço relacionado aos serviços.
     * @param timeSlotAvailabilityService Serviço para verificar a disponibilidade de horários.
     */
    public BookingService(BookingRepository bookingRepository,
                          UserService userService, ServiceService serviceService,
                          TimeSlotAvailabilityService timeSlotAvailabilityService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.serviceService = serviceService;
        this.timeSlotAvailabilityService = timeSlotAvailabilityService;
    }

    /**
     * Realiza o agendamento de um serviço para um usuário.
     *
     * @param userId ID do usuário que está realizando o agendamento.
     * @param data Dados do agendamento solicitados pelo usuário.
     */
    public void bookAppointment(String userId, RequestBookingDTO data) {
        validateUser(userId);
        validateService(data.barberId(), data.serviceId());
        validateAvailability(data.barberId(), data.date(), data.startTime(), data.serviceId());
        Booking booking = createBooking(userId, data.barberId(), data);
        bookingRepository.save(booking);
    }

    /**
     * Recupera todos os agendamentos para um dia específico.
     *
     * @param date Data dos agendamentos a serem recuperados.
     * @return Lista de agendamentos do dia.
     */
    public List<BookingInfoDTO> getAllSchedules(LocalDate date) {
        return bookingRepository.findAllByBarberIdAndDate(null, date);
    }

    /**
     * Cria um objeto de agendamento com base nas informações fornecidas.
     *
     * @param userId ID do usuário.
     * @param barberId ID do barbeiro.
     * @param data Dados do agendamento.
     * @return Objeto de agendamento.
     */
    public Booking createBooking(String userId, String barberId, RequestBookingDTO data) {
        int serviceDuration = serviceService.getDurationById(null, data.serviceId());
        int interval = timeSlotAvailabilityService.getInterval(barberId, data.date());
        LocalTime endTime = data.startTime().plusMinutes(interval * serviceDuration);

        return new Booking(userId, data, endTime);
    }

    /**
     * Valida se o usuário existe no sistema.
     *
     * @param userId ID do usuário a ser validado.
     */
    private void validateUser(String userId) {
        if (!userService.existsUserById(userId)) {
            throw new UserNotFoundException("Usuário não encontrado.");
        }
    }

    /**
     * Valida se o serviço existe para o barbeiro.
     *
     * @param barberId ID do barbeiro.
     * @param serviceId ID do serviço.
     */
    private void validateService(String barberId, String serviceId) {
        if (!serviceService.existsByBarberIdAndId(barberId, serviceId)) {
            throw new ServiceNotFoundException("Serviço não encontrado.");
        }
    }

    /**
     * Valida se o horário e o serviço estão disponíveis para o barbeiro na data especificada.
     *
     * @param barberId ID do barbeiro.
     * @param date Data do agendamento.
     * @param startTime Hora de início do agendamento.
     * @param serviceId ID do serviço.
     */
    private void validateAvailability(String barberId, LocalDate date, LocalTime startTime, String serviceId) {
        if (!timeSlotAvailabilityService.isBkAvailable(barberId, date, startTime, serviceId)) {
            throw new TimeSlotNotAvailableException("Horário não disponível.");
        }
    }

    /**
     * Cancela um agendamento com base no ID do agendamento.
     *
     * @param bookingId ID do agendamento a ser cancelado.
     */
    public void cancelAppointment(String bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if(optionalBooking.isEmpty()) throw new BookingNotFoundException("Agendamento não encontrado.");
        bookingRepository.delete(optionalBooking.get());
    }

    /**
     * Recupera todos os agendamentos de um usuário específico.
     *
     * @param userId ID do usuário.
     * @return Lista de agendamentos do usuário.
     */
    public List<BookingDTO> getAllSchedulesByUser(String userId) {
        return bookingRepository.findAllByBarberIdAndUserId(null, userId);
    }

    /**
     * Recupera o ID do usuário com base no número de telefone fornecido.
     *
     * @param phone Número de telefone do usuário.
     * @return ID do usuário.
     */
    public String getUserIdByPhone(String phone) {
        return userService.findIdByPhone(phone);
    }
}
