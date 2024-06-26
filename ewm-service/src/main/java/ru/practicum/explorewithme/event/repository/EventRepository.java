package ru.practicum.explorewithme.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(Long userId, PageRequest page);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (LOWER(e.annotation) LIKE %?1% " +
            "OR LOWER(e.description) LIKE %?1%) " +
            "AND e.paid = ?2 " +
            "AND e.eventDate > ?3 " +
            "AND e.eventDate < ?4 " +
            "AND e.category IN (?5) " +
            "AND e.state = 'PUBLISHED' " +
            "AND e.confirmedRequests < e.participantLimit")
    List<Event> findAllOnlyAvailable(String text, Boolean paid, LocalDateTime start, LocalDateTime end,
                                     List<Category> categories, PageRequest pageRequest);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (LOWER(e.annotation) LIKE %?1% " +
            "OR LOWER(e.description) LIKE %?1%) " +
            "AND e.eventDate > ?2 " +
            "AND e.eventDate < ?3 " +
            "AND e.category IN (?4) " +
            "AND e.state = 'PUBLISHED' " +
            "AND e.confirmedRequests < e.participantLimit")
    List<Event> findAllOnlyAvailable(String text, LocalDateTime start, LocalDateTime end, List<Category> categories,
                                     PageRequest pageRequest);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (LOWER(e.annotation) LIKE %?1% " +
            "OR LOWER(e.description) LIKE %?1%) " +
            "AND e.paid = ?2 " +
            "AND e.eventDate > ?3 " +
            "AND e.eventDate < ?4 " +
            "AND e.state = 'PUBLISHED' " +
            "AND e.confirmedRequests < e.participantLimit")
    List<Event> findAllOnlyAvailable(String text, Boolean paid, LocalDateTime start, LocalDateTime end,
                                     PageRequest pageRequest);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.paid = ?1 " +
            "AND e.eventDate > ?2 " +
            "AND e.eventDate < ?3 " +
            "AND e.category IN (?4) " +
            "AND e.state = 'PUBLISHED' " +
            "AND e.confirmedRequests < e.participantLimit")
    List<Event> findAllOnlyAvailable(Boolean paid, LocalDateTime start, LocalDateTime end, List<Category> categories,
                                     PageRequest pageRequest);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (LOWER(e.annotation) LIKE %?1% " +
            "OR LOWER(e.description) LIKE %?1%) " +
            "AND e.eventDate > ?2 " +
            "AND e.eventDate < ?3 " +
            "AND e.state = 'PUBLISHED' " +
            "AND e.confirmedRequests < e.participantLimit")
    List<Event> findAllOnlyAvailable(String text, LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.eventDate > ?1 " +
            "AND e.eventDate < ?2 " +
            "AND e.category IN (?3) " +
            "AND e.state = 'PUBLISHED' " +
            "AND e.confirmedRequests < e.participantLimit")
    List<Event> findAllOnlyAvailable(LocalDateTime start, LocalDateTime end, List<Category> categories,
                                     PageRequest pageRequest);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.paid = ?1 " +
            "AND e.eventDate > ?2 " +
            "AND e.eventDate < ?3 " +
            "AND e.state = 'PUBLISHED' " +
            "AND e.confirmedRequests < e.participantLimit")
    List<Event> findAllOnlyAvailable(Boolean paid, LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.eventDate > ?1 " +
            "AND e.eventDate < ?2 " +
            "AND e.state = 'PUBLISHED' " +
            "AND e.confirmedRequests < e.participantLimit")
    List<Event> findAllOnlyAvailable(LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (LOWER(e.annotation) LIKE %?1% " +
            "OR LOWER(e.description) LIKE %?1%) " +
            "AND e.paid = ?2 " +
            "AND e.eventDate > ?3 " +
            "AND e.eventDate < ?4 " +
            "AND e.category IN (?5) " +
            "AND e.state = 'PUBLISHED'")
    List<Event> findAll(String text, Boolean paid, LocalDateTime start, LocalDateTime end, List<Category> categories,
                        PageRequest pageRequest);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (LOWER(e.annotation) LIKE %?1% " +
            "OR LOWER(e.description) LIKE %?1%) " +
            "AND e.eventDate > ?2 " +
            "AND e.eventDate < ?3 " +
            "AND e.category IN (?4) " +
            "AND e.state = 'PUBLISHED'")
    List<Event> findAll(String text, LocalDateTime start, LocalDateTime end, List<Category> categories,
                        PageRequest pageRequest);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (LOWER(e.annotation) LIKE %?1% " +
            "OR LOWER(e.description) LIKE %?1%) " +
            "AND e.paid = ?2 " +
            "AND e.eventDate > ?3 " +
            "AND e.eventDate < ?4 " +
            "AND e.state = 'PUBLISHED'")
    List<Event> findAll(String text, Boolean paid, LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (LOWER(e.annotation) LIKE %?1% " +
            "OR LOWER(e.description) LIKE %?1%) " +
            "AND e.eventDate > ?2 " +
            "AND e.eventDate < ?3 " +
            "AND e.state = 'PUBLISHED'")
    List<Event> findAll(String text, LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.eventDate > ?1 " +
            "AND e.eventDate < ?2 " +
            "AND e.state = 'PUBLISHED'")
    List<Event> findAll(LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.paid = ?1 " +
            "AND e.eventDate > ?2 " +
            "AND e.eventDate < ?3 " +
            "AND e.category IN (?4) " +
            "AND e.state = 'PUBLISHED'")
    List<Event> findAll(Boolean paid, LocalDateTime start, LocalDateTime end, List<Category> categories,
                        PageRequest pageRequest);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.eventDate > ?1 " +
            "AND e.eventDate < ?2 " +
            "AND e.category IN (?3) " +
            "AND e.state = 'PUBLISHED'")
    List<Event> findAll(LocalDateTime start, LocalDateTime end, List<Category> categories, PageRequest pageRequest);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.paid = ?1 " +
            "AND e.eventDate > ?2 " +
            "AND e.eventDate < ?3 " +
            "AND e.state = 'PUBLISHED'")
    List<Event> findAll(Boolean paid, LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    List<Event> findAllByInitiatorInAndStateInAndCategoryInAndEventDateIsAfterAndEventDateIsBefore(List<User> users,
                                                                                                   List<State> states,
                                                                                                   List<Category> categories,
                                                                                                   LocalDateTime start,
                                                                                                   LocalDateTime end,
                                                                                                   PageRequest pageRequest);

    List<Event> findAllByInitiatorInAndStateInAndEventDateIsAfterAndEventDateIsBefore(List<User> users,
                                                                                      List<State> states,
                                                                                      LocalDateTime start,
                                                                                      LocalDateTime end,
                                                                                      PageRequest pageRequest);

    List<Event> findAllByInitiatorInAndCategoryInAndEventDateIsAfterAndEventDateIsBefore(List<User> users,
                                                                                         List<Category> categories,
                                                                                         LocalDateTime start,
                                                                                         LocalDateTime end,
                                                                                         PageRequest pageRequest);

    List<Event> findAllByStateInAndCategoryInAndEventDateIsAfterAndEventDateIsBefore(List<State> states,
                                                                                     List<Category> categories,
                                                                                     LocalDateTime start,
                                                                                     LocalDateTime end,
                                                                                     PageRequest pageRequest);

    List<Event> findAllByInitiatorInAndEventDateIsAfterAndEventDateIsBefore(List<User> users,
                                                                            LocalDateTime start,
                                                                            LocalDateTime end,
                                                                            PageRequest pageRequest);

    List<Event> findAllByStateInAndEventDateIsAfterAndEventDateIsBefore(List<State> states,
                                                                        LocalDateTime start,
                                                                        LocalDateTime end,
                                                                        PageRequest pageRequest);

    List<Event> findAllByCategoryInAndEventDateIsAfterAndEventDateIsBefore(List<Category> categories,
                                                                           LocalDateTime start,
                                                                           LocalDateTime end,
                                                                           PageRequest pageRequest);

    List<Event> findAllByEventDateIsAfterAndEventDateIsBefore(LocalDateTime start, LocalDateTime end,
                                                              PageRequest pageRequest);
}
