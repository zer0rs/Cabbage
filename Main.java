
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.Category;
import static org.dreambot.api.methods.Calculations.random;
import org.dreambot.api.wrappers.interactive.GameObject;


@ScriptManifest(category = Category.MONEYMAKING, name = "CabbageLover", author = "zer0", version = 0.1)


public class Main extends AbstractScript
{
    Area cabbagefield = new Area(3044, 3283, 3067, 3297);
    //Loot l = new Loot();
    //Banking b = new Banking();
    //private Timer timer;
    private State currentState;
    public boolean running = false;
    GameObject cabbage;


    @Override
    public int onLoop()
    {
        log("Script active");
        if(!running)
        {
            return 500;
        }

        currentState = getState();
        getState();
        switch(currentState)
        {
            case BANK:
            {
                Bank();
                break;
            }
            case LOOT:
            {
                Looting();
                break;
            }
            case WALK_B:
            {
                WalkBank();
                break;
            }
            case WALK_C:
            {
                WalkLoot();
                break;
            }
        }
        return random(200, 400);
    }

    @Override
    public void onStart()
    {
        log("skrrt");
        running = true;
    }
    @Override
    public void onExit()
    {
        log("SKRRTTT");
    } //Pretty basic


    private enum State //C = cabbage, B = Bank
    {
        LOOT, BANK, WAIT, WALK_C, WALK_B
    }

    private State getState()
    {
        if(getInventory().isFull() && BankLocation.DRAYNOR.getArea(5).contains(getLocalPlayer())) //bank
        {
            return State.BANK;
        }
        else if(getInventory().isFull() && !BankLocation.DRAYNOR.getArea(5).contains(getLocalPlayer())) //full inv but not in bank
        {
            return State.WALK_B;
        }
        if (!getInventory().isFull() && cabbagefield.contains(getLocalPlayer())) //ready to loot and in field
        {
            return State.LOOT;
        }
        else if(!getInventory().isFull() && !cabbagefield.contains(getLocalPlayer())) //ready to loot , !field
        {
            return State.WALK_C;
        }

        return State.WAIT;
    }

    public void Looting()
    {
        cabbage = getGameObjects().closest(c -> c != null  && c.getName().equalsIgnoreCase("Cabbage") && c.hasAction("Pick"));
        cabbage.interact();
        sleepUntil(() -> getLocalPlayer().isAnimating() || !cabbage.exists(),random(1500,2500));
        sleepUntil(() -> !getLocalPlayer().isAnimating() || !cabbage.exists(),random(1500,2500));
    }

    public void WalkLoot()
    {
        getWalking().walk(cabbagefield.getRandomTile());
    }

    public void Bank()
    {
        getBank().openClosest();
        getBank().depositAll("Cabbage");
    }

    public void WalkBank()
    {
        if(!BankLocation.DRAYNOR.getArea(5).contains(getLocalPlayer()))
        {
            getWalking().walk(BankLocation.DRAYNOR.getCenter());
        }
        else
        {
            log("Error banking");
        }
    }
}

//todo soonTM
/*
* Fix different states instead of functions
* Improved Banking
* Fix paint
* Learn how java works
* im lost
* :(
* */