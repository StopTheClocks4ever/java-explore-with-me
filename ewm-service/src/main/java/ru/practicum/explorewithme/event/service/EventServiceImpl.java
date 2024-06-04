package ru.practicum.explorewithme.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.StatsClient;
import ru.practicum.explorewithme.category.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.common.util.PageCreatorUtil;
import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.event.exception.*;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.Location;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.event.model.StateAction;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.event.repository.LocationRepository;
import ru.practicum.explorewithme.user.exception.UserNotFoundException;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class EventServiceImpl implements EventService {

    private final StatsClient statsClient;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        if (LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER).isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventIsTooSoonException("Нельзя запланировать мероприятие меньше чем за 2 часа до его начала");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new CategoryNotFoundException("Категории с id = " + newEventDto.getCategory() + " не существует"));
        Location existedLocation = locationRepository.findByLatAndLon(newEventDto.getLocation().getLat(), newEventDto.getLocation().getLon());
        if (existedLocation == null) {
            existedLocation = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));
        }
        Event event = eventRepository.save(EventMapper.toEvent(newEventDto, user, category, existedLocation));
        log.info("Событие: " + event + " сохранено");
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> findEventsByCurrentUser(Long userId, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        PageRequest page = PageCreatorUtil.createPage(from, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, page);
        log.info("Найдены события: " + events);
        return EventMapper.listToEventShortDto(events);
    }

    @Override
    public EventFullDto findEventByCurrentUser(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("События с id = " + eventId + " не существует"));
        log.info("Найдено событие: " + event);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("События с id = " + eventId + " не существует"));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        if (event.getInitiator().getId() != userId) {
            throw new NotInitiatorException("Пользователь с id = " + userId + " не может обновлять событие с id = " + eventId);
        }
        if (event.getState() == State.PUBLISHED) {
            throw new EventIsAlreadyPublishedException("Опубликованное событие нельзя изменять");
        }
        if (request.getEventDate() != null) {
            if (LocalDateTime.parse(request.getEventDate(), FORMATTER).isBefore(LocalDateTime.now().plusHours(2))) {
                throw new EventIsTooSoonException("Нельзя изменять мероприятие меньше чем за 2 часа до его начала");
            }
        }

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            event.setCategory(categoryRepository.findById(request.getCategory()).orElseThrow(() -> new CategoryNotFoundException("Категории с id = " + request.getCategory() + " не существует")));
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(request.getEventDate(), FORMATTER));
        }
        if (request.getLocation() != null) {
            event.setLocation(LocationMapper.toLocation(request.getLocation()));
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantsLimit() != null) {
            event.setParticipantLimit(request.getParticipantsLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getStateAction() != null) {
            if (request.getStateAction() == StateAction.SEND_TO_REVIEW) {
                event.setState(State.PENDING);
            } else if (request.getStateAction() == StateAction.CANCEL_REVIEW) {
                event.setState(State.CANCELED);
            } else if (request.getStateAction() == StateAction.PUBLISH_EVENT) {
                event.setState(State.PUBLISHED);
            } else {
                event.setState(State.CANCELED);
            }
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        Event updatedEvent = eventRepository.save(event);
        log.info("Событие: " + event + "обновлено");
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> findEventsPublic(String textParameter, List<Long> categoriesId, Boolean paid, String rangeStartParameter,
                                                String rangeEndParameter, Boolean onlyAvailable, String sortParameter, int from, int size,
                                                HttpServletRequest request) {
        String text;
        LocalDateTime rangeStart;
        LocalDateTime rangeEnd;
        String sort;
        PageRequest page;

        if (textParameter != null) {
            text = textParameter.toLowerCase();
        }
        if (rangeStartParameter != null) {
            rangeStart = LocalDateTime.parse(rangeStartParameter, FORMATTER);
        } else {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEndParameter != null) {
            rangeEnd = LocalDateTime.parse(rangeEndParameter, FORMATTER);
            if (rangeEnd.isBefore(rangeStart)) {
                throw new NotValidException("Окончание мероприятия не может быть раньше его начала");
            }
        } else {
            rangeEnd = LocalDateTime.now().plusMonths(3);
        }
        if (sortParameter != null) {
            if (sortParameter.equals("EVENT_DATE")) {
                sort = "eventDate";
            } else {
                sort = "views";
            }
            page = PageCreatorUtil.createPage(from, size, sort);
        } else {
            page = PageCreatorUtil.createPage(from, size);
        }

        List<Category> categories;
        if (categoriesId != null) {
            categories = categoryRepository.findAllById(categoriesId);
            if (categories.size() == 0) {
                categories = null;
            }
        } else {
            categories = null;
        }

        statsClient.addHit(new EndpointHitDto("ewm-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().format(FORMATTER)));


        return null;
    }


}
