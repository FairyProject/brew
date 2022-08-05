package dev.imanity.brew.util.distribution;

import dev.imanity.brew.game.Game;
import dev.imanity.brew.team.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public interface TeamDistribution {

    @Nullable
    default Team find(@NotNull Game game) {
        return this.find(game, team -> true);
    }

    @Nullable
    Team find(@NotNull Game game, @NotNull Predicate<Team> teamPredicate);

    TeamDistribution EQUALLY = (game, teamPredicate) -> {
        int bestCount = 0;
        Team bestTeam = null;
        for (Team team : game.getTeams()) {
            if (teamPredicate.test(team) && (bestTeam == null || team.getPlayerCount() < bestCount)) {
                bestTeam = team;
                bestCount = team.getPlayerCount();
            }
        }

        return bestTeam;
    };

    TeamDistribution FILLING  = (game, teamPredicate) -> {
        int bestCount = 0;
        Team bestTeam = null;
        for (Team team : game.getTeams()) {
            if (teamPredicate.test(team) && (bestTeam == null || team.getPlayerCount() > bestCount)) {
                bestTeam = team;
                bestCount = team.getPlayerCount();
            }
        }

        return bestTeam;
    };

}
