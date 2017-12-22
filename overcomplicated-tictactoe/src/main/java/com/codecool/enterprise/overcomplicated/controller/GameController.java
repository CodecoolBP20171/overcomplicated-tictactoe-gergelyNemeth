package com.codecool.enterprise.overcomplicated.controller;

import com.codecool.enterprise.overcomplicated.model.Player;
import com.codecool.enterprise.overcomplicated.model.TictactoeGame;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes({"player", "game"})
public class GameController {

    @ModelAttribute("player")
    public Player getPlayer() {
        return new Player();
    }

    @ModelAttribute("game")
    public TictactoeGame getGame() {
        return new TictactoeGame();
    }

    @ModelAttribute("funfact")
    public String getFunfact() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response =
                    restTemplate.getForEntity("http://localhost:60001/funfact", String.class);
            JacksonJsonParser jacksonJsonParser = new JacksonJsonParser();
            return (String) jacksonJsonParser.parseMap(response.getBody()).get("funfact");
        } catch (ResourceAccessException e) {
            System.out.println("FunFact Service is unavailable: " + e);
            return "Chuck Norris knows the last digit of pi.";
        }
    }

    @ModelAttribute("avatar_uri")
    public String getAvatarUri(HttpSession httpSession) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response =
                    restTemplate.getForEntity("http://localhost:60000/avatar?avatarString=" + httpSession.getId(), String.class);
            JacksonJsonParser jacksonJsonParser = new JacksonJsonParser();
            return (String) jacksonJsonParser.parseMap(response.getBody()).get("avatarURI");
        } catch (ResourceAccessException e) {
            System.out.println("Avatar Service is unavailable: " + e);
            return "https://robohash.org/codecool";
        }
    }

    private Integer getAiMove(TictactoeGame game) {
        Integer move = null;
        try {
            String gameTable = String.join("", game.getTable());
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response =
                    restTemplate.getForEntity("http://localhost:60003/ai?table=" + gameTable, String.class);
            if (response.getBody() != null) {
                move = Integer.valueOf(response.getBody());
            }
        } catch (ResourceAccessException e) {
            System.out.println("AI Service is unavailable: " + e);
        }
        return move;
    }

    @GetMapping(value = "/")
    public String welcomeView(@ModelAttribute Player player) {
        return "welcome";
    }

    @PostMapping(value = "/changeplayerusername")
    public String changPlayerUserName(@ModelAttribute Player player) {
        return "redirect:/game";
    }

    @GetMapping(value = "/game")
    public String gameView(@ModelAttribute("player") Player player, Model model,
                           @ModelAttribute("game") TictactoeGame game) {
        game.initGame();
        model.addAttribute("comic_uri", "https://imgs.xkcd.com/comics/bad_code.png");
        return "game";
    }

    @GetMapping(value = "/game/new")
    public String gameView(@ModelAttribute("game") TictactoeGame game) {
        game.startGame();
        return "redirect:/game";
    }

    @GetMapping(value = "/game-move")
    public String gameMove(@ModelAttribute("player") Player player,
                           @ModelAttribute("game") TictactoeGame game,
                           @ModelAttribute("move") int move) {
        game.playerMove(move);
        game.aiMove(getAiMove(game));
        if (game.isGameOver()) {
            return "redirect:gameover";
        }
        return "redirect:game";
    }

    @GetMapping(value = "/gameover")
    public String gameOver(@ModelAttribute("player") Player player,
                           @ModelAttribute("game") TictactoeGame game) {
        if (game.getWinner() == null) return "redirect:/";
        return "gameover";
    }
}
