package restserver;

import core.EntryManager;
import localpersistence.EntrySaverJson;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Gives the server access
 * to methods from core and local-persistence.
 */
@Service
public class GetFitService {
    /**
     * The session's entryManager.
     */
    private final EntryManager entryManager;
    /**
     * Create a new EntryManager object on initialization
     * and loads data from local-persistence.
     */
    public GetFitService() {
        this.entryManager = new EntryManager();
        load();
    }

    /**
     * Converts seconds to hours.
     *
     * @param sec seconds.
     * @return seconds as hours.
     */
    public static String convertFromSecondsToHours(final double sec) {
        final int secToHours = 3600;
        final int magnitude = 10;
        double h = (double) Math.round((sec / secToHours) * magnitude);
        return h / magnitude + "h";
    }

    /**
     * Use EntrySaverJson from local-persistence to load
     * the content of the save file to the entryManager.
     */
    public void load() {
        try {
            EntrySaverJson.load(this.entryManager);
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use EntrySaverJson from local-persistence to save
     * the state of the EntryManager to file.
     */
    public void save() {
        try {
            EntrySaverJson.save(this.entryManager);
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows other classes to access the EntryManager
     * of this GetFitService.
     *
     * @return the EntryManager from this GetFitService
     */
    protected EntryManager getEntryManager() {
        return this.entryManager;
    }
}
