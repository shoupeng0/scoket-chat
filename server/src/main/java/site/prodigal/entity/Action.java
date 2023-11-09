package site.prodigal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ShouPeng
 * @date 2023/10/31 15:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Action {
    private String path;
    private String destination;

    private Object[] params;

    public Action(String path, Object[] params) {
        this.path = path;
        this.params = params;
    }

}
