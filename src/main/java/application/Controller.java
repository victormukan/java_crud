package application;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class Controller {

    private static final Gson converter = new Gson();
    private static List<Greeting> greetings = new ArrayList<>();
    private static final String template = "Hello, %s!";

    @GetMapping(path="/greetings", produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> getAll() {
        return new ResponseEntity<>(converter.toJson(greetings), HttpStatus.OK);
    }

    @GetMapping(path="/greetings/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> getById(@PathVariable int id) {
        try {
            return new ResponseEntity<>(converter.toJson(greetings.get(id)), HttpStatus.OK);
        } catch(IndexOutOfBoundsException e) {
            return new ResponseEntity<>(converter.toJson(e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path="/greetings/{name}", produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> createGreeting(@PathVariable String name) {
        Greeting newGreeting = new Greeting(String.format(template, name));
        greetings.add(newGreeting);

        return new ResponseEntity<>(converter.toJson(newGreeting), HttpStatus.CREATED);
    }

    @PostMapping(path="/greetings", produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> createGreetingUsingBody(@RequestBody String name) {
        Greeting newGreeting = new Greeting(String.format(template, name));
        greetings.add(newGreeting);

        return new ResponseEntity<>(converter.toJson(newGreeting), HttpStatus.CREATED);
    }

    @PostMapping(path="/greetings/object", produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> createGreetingUsingBodyObject(@RequestBody Greeting newGreeting) {
        greetings.add(newGreeting);

        return new ResponseEntity<>(converter.toJson(newGreeting), HttpStatus.CREATED);
    }

    @PutMapping("/greetings/{id}")
    public @ResponseBody ResponseEntity<String> updateGreeting(@PathVariable int id, @RequestBody Greeting newGreeting) {
        try {
            greetings.set(id, newGreeting);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path="/greetings/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> deleteGreeting(@PathVariable int id) {
        Greeting deleteGreeting = greetings.remove(id);

        return new ResponseEntity<>(converter.toJson(deleteGreeting), HttpStatus.OK);
    }
}