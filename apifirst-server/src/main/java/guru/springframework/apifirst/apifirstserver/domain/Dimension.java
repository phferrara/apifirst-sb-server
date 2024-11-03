package guru.springframework.apifirst.apifirstserver.domain;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Dimension {
    private Integer length;
    private Integer width;
    private Integer height;
}
