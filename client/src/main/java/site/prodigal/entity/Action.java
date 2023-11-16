package site.prodigal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Action {
    private String path;
    private String destination;

    private String callback;

    private Object[] params;

    public Action(String path, Object[] params) {
        this.path = path;
        this.params = params;
    }

    public Action(String path, String destination, Object[] params) {
        this.path = path;
        this.destination = destination;
        this.params = params;
    }
}
