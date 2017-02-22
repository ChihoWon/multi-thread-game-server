package vo.bingo.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class RankVO {
	
	private int no;
	private String userId;
	private double userTime;

}
