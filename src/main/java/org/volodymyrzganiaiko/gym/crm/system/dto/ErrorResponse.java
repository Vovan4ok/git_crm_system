package org.volodymyrzganiaiko.gym.crm.system.dto;

import java.time.LocalDateTime;

public record ErrorResponse (int status, String message, LocalDateTime timestamp) {
}
