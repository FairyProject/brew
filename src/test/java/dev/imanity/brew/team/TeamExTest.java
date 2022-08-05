package dev.imanity.brew.team;

import dev.imanity.brew.game.Game;
import io.fairyproject.tests.bukkit.base.BukkitJUnitJupiterBase;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TeamExTest extends BukkitJUnitJupiterBase {

    @Test
    public void getTeamForPlayer() {
        Game game = new Game();
        final Team team = game.createTeam();

        Player player = this.server.addPlayer();
        team.addPlayer(player);

        Assertions.assertEquals(TeamEx.getTeamByPlayer(player), team);
    }

    @Test
    public void createTeamForPlayers() {
        Game game = new Game();
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Player player = this.server.addPlayer();
            game.addPlayer(player);
            players.add(player);
        }

        TeamEx.createTeamForPlayers(game);

        for (Player player : players) {
            final Team team = TeamEx.getTeamByPlayer(player);
            Assertions.assertNotNull(team);
        }
    }

    @Test
    public void assignTeamToPlayers() {
        Game game = new Game();
        game.setMaxPlayerPerTeam(3);

        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            teams.add(game.createTeam());
        }

        for (int i = 0; i < 12; i++) {
            Player player = this.server.addPlayer();
            game.addPlayer(player);
        }

        TeamEx.assignTeamToPlayers(game, false);
        for (Team team : teams) {
            Assertions.assertEquals(team.getPlayerCount(), game.getMaxPlayerPerTeam());
        }
    }

    @Test
    public void assignTeamEqually() {
        Game game = new Game();
        game.setMaxPlayerPerTeam(6);

        Team a = game.createTeam();
        Team b = game.createTeam();

        for (int i = 0; i < 12; i++) {
            Player player = this.server.addPlayer();
            game.addPlayer(player);
            a.addPlayer(player);
        }

        TeamEx.assignTeamEqually(game);
        Assertions.assertEquals(a.getPlayerCount(), game.getMaxPlayerPerTeam());
        Assertions.assertEquals(b.getPlayerCount(), game.getMaxPlayerPerTeam());
    }


}
