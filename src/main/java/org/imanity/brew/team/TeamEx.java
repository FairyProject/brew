package org.imanity.brew.team;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import io.fairyproject.bukkit.metadata.Metadata;
import io.fairyproject.metadata.MetadataKey;
import org.imanity.brew.game.Game;
import org.imanity.brew.team.event.TeamJoinEvent;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class TeamEx {

    public final MetadataKey<Team> TEAM = MetadataKey.create("brew:team", Team.class);

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

    @Nullable
    public Team getTeamByPlayer(Player player) {
        return Metadata.provideForPlayer(player).getOrNull(TEAM);
    }

}
