//package dev.imanity.brew.game;
//
//import dev.imanity.brew.BrewEx;
//import dev.imanity.brew.team.Team;
//import io.fairyproject.bukkit.metadata.Metadata;
//import io.fairyproject.tests.bukkit.base.BukkitJUnitJupiterBase;
//import org.bukkit.entity.Player;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class GameTest extends BukkitJUnitJupiterBase {
//
//    //@Test
//    public void addOrRemovePlayer() {
//        Game game = new Game();
//        Player player = this.server.addPlayer();
//
//        game.addPlayer(player);
//        assertEquals(game, Metadata.provideForPlayer(player).getOrNull(BrewEx.GAME));
//        assertTrue(game.isPlayer(player));
//
//        game.removePlayer(player);
//        assertNull(Metadata.provideForPlayer(player).getOrNull(BrewEx.GAME));
//    }
//
//    //@Test
//    public void createOrRemoveTeam() {
//        Game game = new Game();
//        final Team team = game.createTeam();
//
//        assertTrue(game.hasTeam(team));
//        game.removeTeam(team);
//
//        assertFalse(game.hasTeam(team));
//    }
//
//    //@Test
//    public void getTeamById() {
//        Game game = new Game();
//        Team team = game.createTeam();
//
//        assertSame(team, game.getTeam(team.getId()));
//    }
//
//    //@Test
//    public void createTeamWithIdAlreadyExists() {
//        Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            Game game = new Game();
//
//            game.createTeam(1);
//            game.createTeam(1);
//        });
//    }
//
//    //@Test
//    public void getTeams() {
//        Game game = new Game();
//
//        List<Team> teams = new ArrayList<>();
//        for (int i = 0; i < 12; i++) {
//            final Team team = game.createTeam();
//            teams.add(team);
//        }
//
//        assertEquals(teams.size(), game.getTeams().size());
//    }
//
//}
