package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.ExceptionBadRequest;
import com.game.exception.ExceptionNotFound;
import com.game.repository.PlayersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PlayersService {
    private static final long MAX_YEAR=3000;
    private static final long MIN_YEAR=2000;
    private static final int MAX_NAME_LENGTH=12;
    private static final int MAX_TITLE_LENGTH=30;
    private static final int MAX_EXPERIENCE=10000000;
    private PlayersRepository playersRepository;
    @Autowired
    public PlayersService(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }
    public Page<Player> showAll(Specification<Player> specification, Pageable pageable){
        return playersRepository.findAll(specification,pageable);
    }
    public int showCount(Specification<Player> specification){
        return (int)(playersRepository.count(specification));
    }
    public Player findOne(long id){

        Optional<Player> player= playersRepository.findById(id);
        return player.orElse(null);
    }

    @Transactional
    public Player create(Player player){
        checkCorrectParameters(player);
        player.setLevel(calculateLevel(player.getExperience()));
        player.setUntilNextLevel(calculateUntilNextLevel(player.getExperience(),player.getLevel()));
        return playersRepository.save(player);
    }

    @Transactional
    public Player delete(Long id){
        Player player=getPlayerById(id);
        playersRepository.delete(player);
        return player;
    }

    public Player getPlayerById(Long id){
        if (id<=0) throw new ExceptionBadRequest();
        return playersRepository.findById(id).orElseThrow(()->new ExceptionNotFound());
    }

    public Specification<Player> filterByName(String name){
        return ((root, query, cb) -> name==null?null: cb.like(root.get("name"),"%"+name+"%"));
    }
    public Specification<Player> filterByTitle(String title){
        return ((root, query, cb) -> title==null?null: cb.like(root.get("title"),"%"+title+"%"));
    }
    public Specification<Player> filterByBirthday(Long after,Long before){
        return (root, query, cb) -> {
            if (after==null&&before==null) return null;
            if (before==null) return cb.greaterThanOrEqualTo(root.get("birthday"), new Date(after));
            if (after==null) return cb.lessThanOrEqualTo(root.get("birthday"), new Date(before));
            return cb.between(root.get("birthday"), new Date(after),new Date(before));
        };
    }
    public Specification<Player> filterByLevel(Integer minLevel,Integer maxLevel){
        return (root, query, cb) -> {
            if (minLevel==null&&maxLevel==null) return null;
            if (maxLevel==null) return cb.greaterThanOrEqualTo(root.get("level"), minLevel);
            if (minLevel==null) return cb.lessThanOrEqualTo(root.get("level"), maxLevel);
            return cb.between(root.get("level"),minLevel,maxLevel);
        };
    }
    public Specification<Player> filterByExperience(Integer minExperience,Integer maxExperience){
        return (root, query, cb) -> {
            if (minExperience==null&&maxExperience==null) return null;
            if (maxExperience==null) return cb.greaterThanOrEqualTo(root.get("experience"), minExperience);
            if (minExperience==null) return cb.lessThanOrEqualTo(root.get("experience"), maxExperience);
            return cb.between(root.get("experience"),minExperience,maxExperience);
        };
    }
    public Specification<Player> filterByRace(Race race){
        return ((root, query, cb) -> race==null?null:cb.equal(root.get("race"),race));
    }
    public Specification<Player> filterByProfession(Profession profession){
        return ((root, query, cb) -> profession==null?null:cb.equal(root.get("profession"),profession));
    }
    public Specification<Player> filterByBanned(Boolean banned){
        return ((root, query, cb) -> banned==null?null:cb.equal(root.get("banned"),banned));
    }
    private int calculateLevel(int experience){
        return ((int)(Math.sqrt(2500+200*experience)-50)/100);
    }
    private int calculateUntilNextLevel(int experience,int level){
        return 50*(level+1)*(level+2)-experience;
    }

    private void checkCorrectParameters(Player player){
        checkName(player.getName());
        checkTitle(player.getTitle());
        checkExperience(player.getExperience());
        checkBirthday(player.getBirthday());
        /*if (!(player.getName()==null||player.getName().isEmpty()||player.getName().length()>12))
            if (!(player.getTitle()==null||player.getTitle().isEmpty()||player.getTitle().length()>30))
                if (player.getExperience()>=0&&player.getExperience()<=10000000)
                    if (player.getBirthday()!=null){
                        Calendar calendar=Calendar.getInstance();
                        calendar.setTimeInMillis(player.getBirthday().getTime());
                        if (calendar.get(Calendar.YEAR)>=2000&&calendar.get(Calendar.YEAR)<=3000)
                            return;
                    }
        throw new ExceptionBadRequest();*/
    }

    @Transactional
    public Player update(Player player,long id){
        Player updatedPlayer=getPlayerById(id);

        if (player.getName()!=null){
            checkName(player.getName());
            updatedPlayer.setName(player.getName());
        }
        if (player.getTitle()!=null){
            checkTitle(player.getTitle());
            updatedPlayer.setTitle(player.getTitle());
        }
        if (player.getExperience()!=null){
            checkExperience(player.getExperience());
            updatedPlayer.setExperience(player.getExperience());
            updatedPlayer.setLevel(calculateLevel(updatedPlayer.getExperience()));
            updatedPlayer.setUntilNextLevel(calculateUntilNextLevel(updatedPlayer.getExperience(),updatedPlayer.getLevel()));
        }
        if (player.getBirthday()!=null){
            checkBirthday(player.getBirthday());
            updatedPlayer.setBirthday(player.getBirthday());
        }
        if (player.getRace()!=null){
            updatedPlayer.setRace(player.getRace());
        }
        if (player.getProfession()!=null){
            updatedPlayer.setProfession(player.getProfession());
        }
        updatedPlayer.setBanned(player.isBanned());

        return playersRepository.save(updatedPlayer);
    }

    private void checkName(String name){
        if (name==null||name.isEmpty()||name.length()>MAX_NAME_LENGTH)
            throw  new ExceptionBadRequest();
    }
    private void checkTitle(String title){
        if (title==null||title.isEmpty()||title.length()>MAX_TITLE_LENGTH)
            throw  new ExceptionBadRequest();
    }
    private void checkExperience(int experience){
        if (experience<0||experience>10000000)
            throw  new ExceptionBadRequest();
    }
    private void checkBirthday(Date birthday){
        if (birthday!=null){
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(birthday.getTime());
            if (calendar.get(Calendar.YEAR)>=MIN_YEAR&&calendar.get(Calendar.YEAR)<=MAX_YEAR)
                return ;
        }
        throw  new ExceptionBadRequest();
    }
    /*private void check(){
        throw  new ExceptionBadRequest();
    }*/
}
