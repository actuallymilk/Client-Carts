package lgbt.milk.clientcarts.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class ClientCartsConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("clientcarts.json");
    private static final ClientCartsConfig INSTANCE = load();

    public boolean modEnabled = true;
    public boolean colorEnabled = false;
    public int overlayColor = 0xFFFFFF;
    public int transparency = 50;

    public static ClientCartsConfig get() {
        return INSTANCE;
    }

    public static int renderColor() {
        int rgb = INSTANCE.colorEnabled ? INSTANCE.overlayColor : 0xFFFFFF;
        int alpha = (INSTANCE.transparency * 255 + 50) / 100;
        return alpha << 24 | rgb;
    }

    public static void save() {
        INSTANCE.sanitize();
        Path temporary = PATH.resolveSibling(PATH.getFileName() + ".tmp");
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(temporary, GSON.toJson(INSTANCE), StandardCharsets.UTF_8);
            try {
                Files.move(temporary, PATH, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ignored) {
                Files.move(temporary, PATH, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            System.err.println("ClientCarts could not save its config: " + exception.getMessage());
        }
    }

    private static ClientCartsConfig load() {
        if (!Files.exists(PATH)) {
            return new ClientCartsConfig();
        }
        try {
            ClientCartsConfig config = GSON.fromJson(Files.readString(PATH, StandardCharsets.UTF_8), ClientCartsConfig.class);
            if (config != null) {
                config.sanitize();
                return config;
            }
        } catch (IOException | JsonParseException exception) {
            System.err.println("ClientCarts could not load its config: " + exception.getMessage());
        }
        return new ClientCartsConfig();
    }

    private void sanitize() {
        overlayColor &= 0xFFFFFF;
        transparency = Math.clamp(transparency, 0, 100);
    }
}
