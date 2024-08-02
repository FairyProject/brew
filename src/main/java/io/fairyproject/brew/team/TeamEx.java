package io.fairyproject.brew.team;

import io.fairyproject.brew.FairyBrew;
import io.fairyproject.brew.game.Game;
import io.fairyproject.brew.team.event.TeamJoinEvent;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import io.fairyproject.bukkit.metadata.Metadata;
import io.fairyproject.metadata.MetadataKey;
import org.jetbrains.annotations.Nullable;

@UtilityClass
@Deprecated
public class TeamEx {

    public final MetadataKey<Team> TEAM = FairyBrew.TEAM_KEY;



    @Nullable
    public Team getTeamByPlayer(Player player) {
        return Metadata.provideForPlayer(player).getOrNull(TEAM);
    }

}
