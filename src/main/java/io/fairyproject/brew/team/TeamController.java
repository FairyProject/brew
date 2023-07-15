package io.fairyproject.brew.team;

import io.fairyproject.brew.game.Game;
import io.fairyproject.brew.team.event.TeamJoinEvent;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.entity.Player;

@InjectableComponent
public class TeamController {

    public void createTeamForPlayers(Game game) {
        game.forEach(player -> {
            if (TeamEx.getTeamByPlayer(player) != null) {
                return;
            }

            final Team team = game.createTeam();
            team.addPlayer(player, TeamJoinEvent.Reason.AUTO_ASSIGN);
        });
    }

    public void assignTeamToPlayers(Game game, boolean createTeams) {
        game.forEach(player -> {
            if (TeamEx.getTeamByPlayer(player) != null) {
                return;
            }

            Team target = null;
            for (Team team : game.getTeams()) {
                if (team.getPlayerCount() >= game.getMaxPlayerPerTeam()) {
                    continue;
                }
                if (target == null || team.getPlayerCount() < target.getPlayerCount()) {
                    target = team;
                }
            }

            if (target == null) {
                if (createTeams) {
                    final Team team = game.createTeam();
                    team.addPlayer(player, TeamJoinEvent.Reason.AUTO_ASSIGN);
                    return;
                }
                throw new RuntimeException("There is no team for player to join.");
            }
            target.addPlayer(player);
        });
    }

    public void assignTeamEqually(Game game) {
        while (true) {
            Team largest = null;
            Team smallest = null;

            for (Team team : game.getTeams()) {
                if (largest == null) {
                    largest = team;
                    continue;
                }
                if (largest.getPlayerCount() < team.getPlayerCount()) {
                    Team old = largest;
                    largest = team;

                    team = old;
                }
                if (smallest == null || smallest.getPlayerCount() > team.getPlayerCount()) {
                    smallest = team;
                }
            }

            if (largest == null || smallest == null) {
                break;
            }

            if (largest.getPlayerCount() - smallest.getPlayerCount() <= 1) {
                break;
            }

            while (largest.getPlayerCount() - smallest.getPlayerCount() > 1) {
                final Player player = largest.getFirstPlayer();
                if (player == null) {
                    break;
                }

                smallest.addPlayer(player, TeamJoinEvent.Reason.FORCE_ASSIGN);
            }
        }
    }

}
