CREATE TABLE timeslot_override_closed_slots (
                                                timeslot_override_id TEXT NOT NULL,
                                                closed_slots TEXT NOT NULL,
                                                PRIMARY KEY (timeslot_override_id, closed_slots),
                                                FOREIGN KEY (timeslot_override_id) REFERENCES timeslot_override(id)
);
