package org.imanity.brew.team.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import io.fairyproject.bukkit.listener.events.PlayerCancellableEvent;
import org.imanity.brew.team.Team;

@Getter
public class TeamQuitEvent extends PlayerCancellableEvent {

    private final Reason reason;
    private final Team team;

    public TeamQuitEvent(Player who, Reason reason, Team team) {
        super(who);
        this.reason = reason;
        this.team = team;
    }

    public enum Reason {

        /**
         * Let the player quit by Plugin code, default Reason
         */
        PLUGIN,

        /**
         * Player manually decided to quit the team
         */
        MANUAL_QUIT,

        /**
         * Player disconnected from the server
         */
        DISCONNECTED,

        /**
         * Player joined another team
         */
        JOIN_ANOTHER_TEAM

    }

}
