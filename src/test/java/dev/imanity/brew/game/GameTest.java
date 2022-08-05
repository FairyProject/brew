package dev.imanity.brew.game;

import be.seeseemelk.mockbukkit.Coordinate;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.imanity.brew.game.state.GameStateSequences;
import dev.imanity.brew.game.state.impl.TimedGameState;
import io.fairyproject.bukkit.metadata.Metadata;
import io.fairyproject.tests.bukkit.base.BukkitJUnitJupiterBase;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import dev.imanity.brew.BrewEx;
import dev.imanity.brew.game.state.GameStateBase;
import dev.imanity.brew.listener.ListenerPresets;
import dev.imanity.brew.team.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest extends BukkitJUnitJupiterBase {

    @Test
    public void addOrRemovePlayer() {
        Game game = new Game();
        Player player = this.server.addPlayer();

        game.addPlayer(player);
        assertEquals(game, Metadata.provideForPlayer(player).getOrNull(BrewEx.GAME));
        assertTrue(game.isPlayer(player));

        game.removePlayer(player);
        assertNull(Metadata.provideForPlayer(player).getOrNull(BrewEx.GAME));
    }

    @Test
    public void createOrRemoveTeam() {
        Game game = new Game();
        final Team team = game.createTeam();

        assertTrue(game.hasTeam(team));
        game.removeTeam(team);

        assertFalse(game.hasTeam(team));
    }

    @Test
    public void getTeamById() {
        Game game = new Game();
        Team team = game.createTeam();

        assertSame(team, game.getTeam(team.getId()));
    }

    @Test
    public void createTeamWithIdAlreadyExists() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Game game = new Game();

            game.createTeam(1);
            game.createTeam(1);
        });
    }

    @Test
    public void getTeams() {
        Game game = new Game();

        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            final Team team = game.createTeam();
            teams.add(team);
        }

        assertEquals(teams.size(), game.getTeams().size());
    }

    @Test
    public void state() {
        Game game = new Game();
        PlayerMock player = this.server.addPlayer();
        WorldMock world = this.server.addSimpleWorld("gameWorld");
        game.addPlayer(player);

        GameStateSequences states = new GameStateSequences(game, 1) {
            @Override
            protected void onEnded() {
                player.sendMessage("Ended");
            }
        };
        final TimedGameState a = new TimedGameState(game, 50 * 20L) {
            @Override
            public boolean isConnectable() {
                return true;
            }

            @Override
            protected void onStart() {
                super.onStart();

                player.sendMessage("Start State A");
                ListenerPresets.disallowBlockModification(this);
            }

            @Override
            public String toString() {
                return "State A";
            }
        };
        final GameStateBase b = new GameStateBase(game) {
            @Override
            protected void onStart() {
                this.listenPlayer(PlayerDeathEvent.class)
                        .listen(event -> states.skip())
                        .build();
            }

            @Override
            protected void onUpdate() {

            }

            @Override
            protected void onEnded() {

            }

            @Override
            public boolean isConnectable() {
                return false;
            }

            @Override
            public String toString() {
                return "State B";
            }
        };

        states.add(a);
        states.add(b);

        game.setState(states);
        game.start();
        this.server.getScheduler().performOneTick();

        assertEquals("Start State A", player.nextMessage());
        assertTrue(player.simulateBlockBreak(world.createBlock(new Coordinate())).isCancelled()); // Disallowed block modification at state A
        a.duration(0L);

        this.server.getScheduler().performOneTick();

        assertSame(b, states.getCurrentState());
        player.damage(player.getMaxHealth());

        this.server.getScheduler().performOneTick();
        assertTrue(states.isEnded());
        assertEquals(player.nextMessage(), "Ended");
    }

}
