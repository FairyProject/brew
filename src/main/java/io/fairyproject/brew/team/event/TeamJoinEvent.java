package io.fairyproject.brew.team.event;

import io.fairyproject.brew.team.Team;
import lombok.Getter;
import org.bukkit.entity.Player;
import io.fairyproject.bukkit.listener.events.PlayerCancellableEvent;

@Getter
public class TeamJoinEvent extends PlayerCancellableEvent implements TeamEvent {

    private final Reason reason;
    private final Team team;

    public TeamJoinEvent(Player who, Reason reason, Team team) {
        super(who);
        this.reason = reason;
        this.team = team;
    }

    public enum Reason {

        /**
         * Let the player join by Plugin code, default Reason
         */
        PLUGIN,

        /**
         * Player manually decide to select the team
         */
        MANUAL_SELECT,

        /**
         * Automatically assigned to the team by system due to game start but player wasn't in any team
         */
        AUTO_ASSIGN,

        /**
         * Force assigned due to several system reasons such as Team is not properly distributed
         */
        FORCE_ASSIGN

    }

}
