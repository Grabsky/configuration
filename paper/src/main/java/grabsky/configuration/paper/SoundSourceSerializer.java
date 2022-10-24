package grabsky.configuration.paper;

import grabsky.configuration.serializers.CaseInsensitiveEnumSerializer;
import net.kyori.adventure.sound.Sound;

/** Converts {@link String} to {@link Sound.Source} and vice-versa but using case-insensitive strategy. */
public class SoundSourceSerializer extends CaseInsensitiveEnumSerializer<Sound.Source> {

    /** Default instance of {@link SoundSourceSerializer}. */
    public static final SoundSourceSerializer INSTANCE = new SoundSourceSerializer();
}
