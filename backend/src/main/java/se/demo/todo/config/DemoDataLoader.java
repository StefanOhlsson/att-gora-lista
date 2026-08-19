package se.demo.todo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.demo.todo.domain.Priority;
import se.demo.todo.domain.TodoStatus;
import se.demo.todo.dto.TodoRequest;
import se.demo.todo.repository.TodoRepository;
import se.demo.todo.service.TodoService;

@Configuration
@RequiredArgsConstructor
public class DemoDataLoader {
    private final TodoRepository repository;

    @Bean
    CommandLineRunner loadDemoData(TodoService service) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }
            service.create(new TodoRequest("Förbered presentation", "Gå igenom demo-flödet och säkerställ att allt fungerar inför publiken.", Priority.HOG, TodoStatus.REGISTRERAD));
            service.create(new TodoRequest("Starta backend", "Verifiera att Spring Boot startar och att Swagger är åtkomligt.", Priority.NORMAL, TodoStatus.STARTAD));
            service.create(new TodoRequest("Designa kortlayout", "Skapa en enhetlig kortlayout med avrundade hörn och mjuka skuggor.", Priority.HOG, TodoStatus.STARTAD));
            service.create(new TodoRequest("Koppla PostgreSQL", "Säkerställ att databaskopplingen fungerar via Docker Compose.", Priority.KRITISK, TodoStatus.REGISTRERAD));
            service.create(new TodoRequest("Visa statuslogg", "Demonstrera hur varje statusändring loggas automatiskt.", Priority.KRITISK, TodoStatus.STARTAD));
            service.create(new TodoRequest("Skriv README", "Dokumentera startinstruktioner och demo-flöde.", Priority.NORMAL, TodoStatus.AVSLUTAD));
            service.create(new TodoRequest("Lägg till filtrering", "Filtrera uppgifter på status och prioritet.", Priority.NORMAL, TodoStatus.REGISTRERAD));
            service.create(new TodoRequest("Lägg till sökning", "Sök på rubrik och beskrivning direkt i gränssnittet.", Priority.LAG, TodoStatus.REGISTRERAD));
            service.create(new TodoRequest("Köra tester", "Visa JUnit och Mockito för service- och controllerlogik.", Priority.HOG, TodoStatus.STARTAD));
            service.create(new TodoRequest("Genomför live-demo", "Ändra status live och visa historiken direkt i gränssnittet.", Priority.KRITISK, TodoStatus.REGISTRERAD));

            repository.findAll().stream().limit(4).forEach(item -> {
                if (item.getStatus() == TodoStatus.STARTAD) {
                    service.updateStatus(item.getId(), TodoStatus.AVSLUTAD);
                    service.updateStatus(item.getId(), TodoStatus.STARTAD);
                }
            });
        };
    }
}
