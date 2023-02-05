package projectbusan.gongda.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultDTO<T> {
    private T data;
}
