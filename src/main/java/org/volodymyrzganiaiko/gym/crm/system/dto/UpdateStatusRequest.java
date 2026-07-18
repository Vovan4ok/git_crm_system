package org.volodymyrzganiaiko.gym.crm.system.dto;

import javax.validation.constraints.NotNull;

public record UpdateStatusRequest (@NotNull Boolean isActive) {
}
