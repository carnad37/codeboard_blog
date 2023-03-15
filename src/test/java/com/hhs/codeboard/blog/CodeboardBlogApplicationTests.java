//package com.hhs.codeboard.blog;
//
//import com.hhs.codeboard.blog.jpa.entity.board.dto.request.BoardArticleRequest;
//import com.hhs.codeboard.blog.util.pagination.BoardPagination;
//import com.hhs.codeboard.blog.web.service.board.BoardArticleService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//
////@AutoConfigureMockMvc
//@SpringBootTest
//class CodeboardBlogApplicationTests {
//
////	@Autowired
////	private MockMvc mockMvc;
//
////	@Autowired
////	private ArticleDAO articleDAO;
//
//	@Autowired
//	private BoardArticleService articleService;
//
//	@Test
//	public void test () {
//		BoardPagination pagination = new BoardPagination(10);
//		pagination.init(20);
//		// jpa 테스트
////		List<BoardArticleEntity> result = articleDAO.findAllByBoardSeq(1, pagination.getPageable());
////		case "title" -> resultQuery = permitAdd(resultQuery, inTitle(searchDto.getSearchKeyword()));
////		case "publicFlag" -> resultQuery = permitAdd(resultQuery, isPublic(searchDto.getSearchKeyword()));
////		case "startDate" -> resultQuery = permitAdd(resultQuery, graterDate(LocalDateTime.parse(searchDto.getSearchKeyword(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
////		case "endDate" -> resultQuery = permitAdd(resultQuery, lessDate(LocalDateTime.parse(searchDto.getSearchKeyword(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
////		case "content" -> resultQuery = permitAdd(resultQuery, inContent(searchDto.getSearchKeyword()));
////		List<SearchDto> testList = new ArrayList<>();
////		SearchDto search1 = new SearchDto();
////		search1.setSearchCondition("title");
////		search1.setSearchKeyword("테스트");
////
////		SearchDto search2 = new SearchDto();
////		search2.setSearchCondition("startDate");
////		search2.setSearchKeyword("2022-01-01");
////
////		SearchDto search4 = new SearchDto();
////		search4.setSearchCondition("endDate");
////		search4.setSearchKeyword("2022-01-02");
////
////		SearchDto search3 = new SearchDto();
////		search3.setSearchCondition("publicFlag");
////		search3.setSearchKeyword("Y");
////
////		testList.add(search1);
////		testList.add(search2);
////		testList.add(search3);
////		testList.add(search4);
//
//		BoardArticleRequest boardArticleRequest = new BoardArticleRequest();
//		boardArticleRequest.setBoardSeq(1);
//		boardArticleRequest.setTitle("테스트");
//		boardArticleRequest.setPublicFlag("Y");
//		boardArticleRequest.setSearchStartDate(LocalDate.now());
//
//		System.out.println(articleService.selectArticle(boardArticleRequest));
//	}
//
//	@Test
//	public void test2() {
//
//		Arrays.stream(Test2.class.getMethods()).forEach(
//			method -> System.out.println(method.getName())
//		);
//
//		System.out.println("test end");
//	}
//
//	static class Test1 {
//		public String getValue() {
//			return "1";
//		}
//
//	}
//
//	static class Test2 extends Test1 {
//		public String getValue2() {
//			return "2";
//		}
//	}
//
////	@Autowired
////	private MockMvc mockMvc;
////
////	@Test
////	void loginTest() throws Exception{
////
////		this.mockMvc.perform(
////				post("/gw/login")
////						.param("email", "test@test.co.kr")
////						.param("password", "password")
////		).andExpect(status().isOk()).andDo(print());
////
////	}
//
//
//
//
//}
