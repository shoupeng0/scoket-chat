package site.prodigal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ShouPeng
 * @date 2023/11/8 9:47
 * @email 7698627@qq.com
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRecord {

    private Integer id;
    private String sendUsername;
    private String receiveUsername;
    private String message;

}
