package site.prodigal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since  2023/10/31 15:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private String data;

    private String callback;

}
