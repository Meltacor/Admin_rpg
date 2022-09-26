package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayersRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {
    Optional<Player> findById(long l);

    void deleteById(long id);

}
