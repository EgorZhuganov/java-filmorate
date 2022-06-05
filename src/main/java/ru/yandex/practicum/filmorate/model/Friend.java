package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Friend implements BaseModel<Long> {

    private Long id;
    private Long userId;
    private Long friendId;

}
