package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.gods.Prometheus;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.view.View;

/**
 * This class notifies the worker's it that the player with Prometheus decides to use while using his power
 */
public class SetPrometheus extends Message{
    final int usedWorkerId;

    /**
     * Class constructor
     * {@inheritDoc}
     * @param usedWorkerId is the selected worker's id
     */
    public SetPrometheus(Player player, View view, int usedWorkerId) {
        super(player, view);
        this.usedWorkerId = usedWorkerId;
    }

    /**
     * It sets the selected worker id in the Prometheus' instance,
     * then it sets the model's next phase, message type, and message
     * and finally it asks the model to notify the changes to the views
     * @param controller is the game controller's instance
     */
    @Override
    public void handler(Controller controller) {
        ((Prometheus)player.getGodCard()).setWorkerID(usedWorkerId);
        player.setUsedWorker(usedWorkerId);
        Model model = controller.getModel();
        if(model.getGCPlayer(Gods.PROMETHEUS).getWorker(usedWorkerId).getStatus()){
            model.setNextPhase(Phase.BUILD);
            model.setNextPlayerMessage(PlayerMessage.BUILD);
            model.setNextMessageType(MessageType.BUILD);
            model.notifyChanges();
        }
        else{
            model.setNextPhase(Phase.PROMETHEUS_WORKER);
            model.setNextPlayerMessage(PlayerMessage.PROMETHEUS_ASK_WORKER);
            model.setNextMessageType(MessageType.PROMETHEUS);
            this.view.reportError("The selected worker can't move");
            model.notifyChanges();
        }

    }
}
