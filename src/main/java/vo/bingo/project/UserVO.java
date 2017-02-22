package vo.bingo.project;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class UserVO {
	
	private int no;
	private String userId;		// User id
	private String userPw;		// User password
	private Date writeDate;		// Date of SignUp
	
	public UserVO(String userId, String userPw) {
		this.userId = userId;
		this.userPw = userPw;
	}
	
}
