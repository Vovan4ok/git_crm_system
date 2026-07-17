package org.volodymyrzganiaiko.gym.crm.system.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public record UpdateTrainerListRequest(@NotNull List<String> usernames) {
}
