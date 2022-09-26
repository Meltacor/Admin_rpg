package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/rest/players")
public class PlayersController {

    private PlayersService playersService;
    @Autowired
    public PlayersController(PlayersService playersService) {
        this.playersService = playersService;
    }

    @GetMapping()
    public List<Player> showPlayers(//HttpServletRequest request
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false)String title,
                                    @RequestParam(required = false) Race race,
                                    @RequestParam(required = false) Profession profession,
                                    @RequestParam(required = false)Long after,
                                    @RequestParam(required = false)Long before,
                                    @RequestParam(required = false)Boolean banned,
                                    @RequestParam(required = false)Integer minExperience,
                                    @RequestParam(required = false)Integer maxExperience,
                                    @RequestParam(required = false)Integer minLevel,
                                    @RequestParam(required = false)Integer maxLevel,
                                    @RequestParam(defaultValue="0")Integer pageNumber,
                                    @RequestParam(defaultValue = "3")Integer pageSize,
                                    @RequestParam(defaultValue = "ID")PlayerOrder order)
    {
        Pageable pageable=PageRequest.of(pageNumber,pageSize,Sort.by(order.getFieldName()));
        return playersService.showAll(Specification
                .where(playersService.filterByName(name))
                .and(playersService.filterByTitle(title))
                .and(playersService.filterByRace(race))
                .and(playersService.filterByProfession(profession))
                .and(playersService.filterByLevel(minLevel,maxLevel))
                .and(playersService.filterByExperience(minExperience,maxExperience))
                .and(playersService.filterByBirthday(after,before))
                .and(playersService.filterByBanned(banned)),pageable).getContent();
    }
    @GetMapping("/count")
    public Integer showCount (@RequestParam(required = false) String name,
                              @RequestParam(required = false)String title,
                              @RequestParam(required = false) Race race,
                              @RequestParam(required = false) Profession profession,
                              @RequestParam(required = false)Long after,
                              @RequestParam(required = false)Long before,
                              @RequestParam(required = false)Boolean banned,
                              @RequestParam(required = false)Integer minExperience,
                              @RequestParam(required = false)Integer maxExperience,
                              @RequestParam(required = false)Integer minLevel,
                              @RequestParam(required = false)Integer maxLevel)
    {
        return playersService.showCount(Specification
                .where(playersService.filterByName(name))
                .and(playersService.filterByTitle(title))
                .and(playersService.filterByRace(race))
                .and(playersService.filterByProfession(profession))
                .and(playersService.filterByLevel(minLevel,maxLevel))
                .and(playersService.filterByExperience(minExperience,maxExperience))
                .and(playersService.filterByBirthday(after,before))
                .and(playersService.filterByBanned(banned)));

        //return playersService.getCount();

    }
    @PostMapping()
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        playersService.create(player);

        return ResponseEntity.ok(player);
    }
    @PostMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id,@RequestBody Player player) {
        //player.setId(id);

        return ResponseEntity.ok(playersService.update(player,id));


    }
    @GetMapping("/{id}")
    public ResponseEntity<Player> showPerson(@PathVariable Long id){
            return ResponseEntity.ok(playersService.getPlayerById(id));
    }
    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable long id){
        playersService.delete(id);
            //return (ResponseEntity<Player>) ResponseEntity.ok();

    }

}
