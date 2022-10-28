package grabsky.configuration.paper;

import grabsky.configuration.serializers.CaseInsensitiveEnumSerializer;
import net.kyori.adventure.sound.Sound;

/** Converts {@link String} to {@link Sound.Source} and vice-versa but using case-insensitive strategy. */
public final class SoundSourceSerializer implements CaseInsensitiveEnumSerializer<Sound.Source> {

    /** Default instance of {@link SoundSourceSerializer}. */
    public static final SoundSourceSerializer INSTANCE = new SoundSourceSerializer();
}
