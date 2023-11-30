package io.github.thatkawaiisam.assemble;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class AssembleUtils {

	public String[] splitTeamText(String input) {
		final int inputLength = input.length();
		if (inputLength > 16) {
			String prefix = input.substring(0, 16);
			final int lastColorIndex = prefix.lastIndexOf(ChatColor.COLOR_CHAR);

			String suffix;
			if (lastColorIndex >= 14) {
				prefix = prefix.substring(0, lastColorIndex);
				suffix = ChatColor.getLastColors(input.substring(0, 17)) + input.substring(lastColorIndex + 2);
			} else {
				suffix = ChatColor.getLastColors(prefix) + input.substring(16);
			}

			if (suffix.length() > 16) {
				suffix = suffix.substring(0, 16);
			}

			return new String[]{prefix, suffix};
		} else {
			return new String[]{input, ""};
		}
	}
}
