package org.volodymyrzganiaiko.gym.crm.system.dto;

import javax.validation.constraints.NotBlank;

public record ChangePasswordRequest(@NotBlank String newPassword) {
}
