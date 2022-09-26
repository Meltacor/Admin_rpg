package com.game.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    @Enumerated(value= EnumType.STRING)
    private Race race;
    @Enumerated(value= EnumType.STRING)
    private Profession profession;
    private int level;
    private int untilNextLevel;
    @Temporal(TemporalType.DATE)
    private Date birthday;
    private boolean banned;
    private Integer experience;

    public Player() {
    }

    public Player(String name, String title, Race race, Profession profession, int level, int untilNextLevel, Date birthday, boolean banned, Integer experience) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.level = level;
        this.untilNextLevel = untilNextLevel;
        this.birthday = birthday;
        this.banned = banned;
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(int untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
