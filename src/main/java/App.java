import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";


    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/admins", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("books", Books.all());
      model.put("overdues", Checkout.getOverdue());
      model.put("template", "templates/Admins.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/admins/books/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String title = request.queryParams("bookTitle");
      int copies = Integer.parseInt(request.queryParams("bookCopies"));
      Books newBook = new Books(title, copies);
      newBook.save();

      model.put("books", Books.all());
      model.put("overdues", Checkout.getOverdue());
      model.put("template", "templates/Admins.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/admins/books/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Books book = Books.find(Integer.parseInt(request.params("id")));

      model.put("book", book);
      model.put("authors", book.getAuthors());
      model.put("template", "templates/Book.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/admins/books/:id/author-add", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Books book = Books.find(Integer.parseInt(request.params("id")));
      String firstName = request.queryParams("authorFirstName");
      String lastName = request.queryParams("authorLastName");
      Authors newAuthor = new Authors(firstName, lastName);
      newAuthor.save();
      book.addAuthor(newAuthor.getId());

      model.put("book", book);
      model.put("authors", book.getAuthors());
      model.put("template", "templates/Book.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/patrons", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("patrons", Patrons.all());
      model.put("template", "templates/Patrons.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
