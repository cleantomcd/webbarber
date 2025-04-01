package com.webbarber.webbarber.entity;

import com.webbarber.webbarber.dto.ServiceDTO;
import jakarta.persistence.*;

/**
 * Entidade que representa um serviço oferecido por um barbeiro, incluindo nome, descrição, duração, preço e status ativo.
 * Esta classe é mapeada para a tabela "services" no banco de dados.
 */
@Entity(name = "Service")
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String barberId;

    private String name;
    private String description;
    private int duration;
    private int priceInCents;
    private boolean active;

    /**
     * Construtor para criar um serviço com todos os parâmetros necessários.
     *
     * @param barberId ID do barbeiro responsável pelo serviço.
     * @param name Nome do serviço.
     * @param description Descrição do serviço.
     * @param duration Duração do serviço em minutos.
     * @param priceInCents Preço do serviço em centavos.
     */
    public Service(String barberId, String name, String description, int duration, int priceInCents) {
        this.barberId = barberId;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.priceInCents = priceInCents;
        this.active = true;
    }

    /**
     * Construtor para criar um serviço com parâmetros essenciais, deixando a descrição como nula.
     *
     * @param barberId ID do barbeiro responsável pelo serviço.
     * @param name Nome do serviço.
     * @param duration Duração do serviço em minutos.
     * @param priceInCents Preço do serviço em centavos.
     */
    public Service(String barberId, String name, int duration, int priceInCents) {
        this.barberId = barberId;
        this.name = name;
        this.duration = duration;
        this.priceInCents = priceInCents;
        this.active = true;
    }

    /**
     * Construtor que cria um serviço a partir de um objeto {@link ServiceDTO}.
     *
     * @param barberId ID do barbeiro responsável pelo serviço.
     * @param serviceDTO Objeto que contém os dados do serviço.
     */
    public Service(String barberId, ServiceDTO serviceDTO) {
        this.barberId = barberId;
        this.name = serviceDTO.name();
        this.description = serviceDTO.description();
        this.duration = serviceDTO.duration();
        this.priceInCents = serviceDTO.priceInCents();
        this.active = true;
    }

    /**
     * Construtor padrão necessário para a JPA.
     */
    public Service() {
    }

    /**
     * Obtém o ID do serviço.
     *
     * @return O ID do serviço.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtém o nome do serviço.
     *
     * @return O nome do serviço.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtém a descrição do serviço.
     *
     * @return A descrição do serviço.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Obtém a duração do serviço em minutos.
     *
     * @return A duração do serviço.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Altera o nome do serviço.
     *
     * @param name Novo nome do serviço.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Altera a descrição do serviço.
     *
     * @param description Nova descrição do serviço.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Altera a duração do serviço.
     *
     * @param duration Nova duração do serviço em minutos.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Altera o preço do serviço em centavos.
     *
     * @param priceInCents Novo preço do serviço em centavos.
     */
    public void setPriceInCents(int priceInCents) {
        this.priceInCents = priceInCents;
    }

    /**
     * Obtém o preço do serviço em centavos.
     *
     * @return O preço do serviço em centavos.
     */
    public int getPriceInCents() {
        return this.priceInCents;
    }

    /**
     * Verifica se o serviço está ativo.
     *
     * @return Um valor booleano indicando se o serviço está ativo.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Altera o status de atividade do serviço.
     *
     * @param status O novo status de atividade do serviço.
     */
    public void setActive(boolean status) {
        this.active = status;
    }
}
