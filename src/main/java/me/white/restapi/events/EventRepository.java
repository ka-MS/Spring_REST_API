package me.white.restapi.events;

import org.springframework.data.jpa.repository.JpaRepository;
public interface EventRepository extends JpaRepository<Event, Integer> {

}
