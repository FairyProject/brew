package org.imanity.brew.game;

import be.seeseemelk.mockbukkit.Coordinate;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.fairyproject.bukkit.metadata.Metadata;
import io.fairyproject.tests.bukkit.BukkitTestingBase;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.imanity.brew.constant.PlayerConstants;
import static org.junit.Assert.*;

import org.imanity.brew.game.state.GameStateBase;
import org.imanity.brew.game.state.GameStateSequences;
import org.imanity.brew.game.state.impl.TimedGameState;
import org.imanity.brew.listener.ListenerPresets;
import org.imanity.brew.team.Team;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GameTest extends BukkitTestingBase {

    @Test
    public void addOrRemovePlayer() {
        Game game = new Game();
        Player player = SERVER.addPlayer();

        game.addPlayer(player);
        assertEquals(game, Metadata.provideForPlayer(player).getOrNull(PlayerConstants.GAME));
        assertTrue(game.isPlayer(player));

        game.removePlayer(player);
        assertNull(Metadata.provideForPlayer(player).getOrNull(PlayerConstants.GAME));
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
        PlayerMock player = SERVER.addPlayer();
        WorldMock world = SERVER.addSimpleWorld("gameWorld");
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

        assertEquals("Start State A", player.nextMessage());
        assertTrue(player.simulateBlockBreak(world.createBlock(new Coordinate())).isCancelled()); // Disallowed block modification at state A
        a.duration(0L);

        SERVER.getScheduler().performOneTick();

        assertSame(b, states.getCurrentState());
        player.damage(player.getMaxHealth());

        SERVER.getScheduler().performOneTick();
        assertTrue(states.isEnded());
        assertEquals(player.nextMessage(), "Ended");
    }

}
