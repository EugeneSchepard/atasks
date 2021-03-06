package secondtask.POCtest;

import com.codeborne.selenide.Configuration;
import org.junit.Test;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.CollectionCondition.*;

/**
 * Second task with CRUD tests.
 */

public class TodoMvcTestFull {

    @Test
    public void tasksSecondaryFlow() {

        open("https://todomvc4tasj.herokuapp.com/");

        // create
        $("#new-todo").setValue("a").pressEnter();
        $("#new-todo").setValue("b").pressEnter();
        $("#new-todo").setValue("c").pressEnter();
        $("#new-todo").setValue("d").pressEnter();
        $$("#todo-list>li").shouldHave(exactTexts("a", "b", "c", "d"));

        //(optional) counter = 4
        $("#todo-count>*").shouldHave(exactText("4"));

        //(optional) filter 'Active'
        $$("#filters>li").findBy(exactText("Active")).click();
        $("#todo-list").shouldHave(cssClass("filter-active"));
        $$("#todo-list>li").shouldHave(exactTexts("a", "b", "c", "d"));
        $$("#filters>li").findBy(exactText("All")).click();
        $("#todo-list").shouldNotHave(cssClass("filter-active"));
        $$("#todo-list>li").shouldHave(exactTexts("a", "b", "c", "d"));

        //complete
        $$("#todo-list>li").findBy(exactText("b")).find(".toggle").click();
        $$("#todo-list>li").filterBy(cssClass("completed")).shouldHave(exactTexts("b"));
        $$("#todo-list>li").excludeWith(cssClass("completed")).shouldHave(exactTexts("a", "c", "d"));

        //(optional) filter 'Completed'
        $$("#filters>li").findBy(exactText("Completed")).click();
        $("#todo-list").shouldHave(cssClass("filter-completed"));
        $$("#todo-list>li").shouldHave(exactTexts("", "b", "", ""));
        $$("#filters>li").findBy(exactText("All")).click();
        $("#todo-list").shouldNotHave(cssClass("filter-completed"));
        $$("#todo-list>li").shouldHave(exactTexts("a", "b", "c", "d"));

        //completed > active
        $$("#todo-list>li").findBy(exactText("c")).find(".toggle").click();
        $$("#todo-list>li").filterBy(cssClass("completed")).shouldHave(exactTexts("b", "c"));
        $$("#todo-list>li").excludeWith(cssClass("completed")).shouldHave(exactTexts("a", "d"));
        $$("#todo-list>li").filterBy(cssClass("completed")).findBy(exactText("c")).find(".toggle").click();
        $$("#todo-list>li").filterBy(cssClass("completed")).shouldHave(exactTexts("b"));
        $$("#todo-list>li").excludeWith(cssClass("completed")).shouldHave(exactTexts("a", "c", "d"));

        //update completed
        $$("#todo-list>li").filterBy(cssClass("completed")).findBy(exactText("b")).doubleClick();
        $("#todo-list>li.editing").find(".edit").setValue("bb").pressEnter();
        $$("#todo-list>li").shouldHave(exactTexts("a", "bb", "c", "d"));

        //update active
        $$("#todo-list>li").findBy(exactText("a")).doubleClick();
        $("#todo-list>li.editing").find(".edit").setValue("aa").pressEnter();
        $$("#todo-list>li").shouldHave(exactTexts("aa", "bb", "c","d"));

        //delete completed by button
        $("#todo-list>li.completed").hover().find(".destroy").click();
        $$("#todo-list>li").shouldHave(exactTexts("aa", "c","d"));

        //delete active by text clearing
        $$("#todo-list>li").findBy(exactText("aa")).doubleClick();
        $("#todo-list>li.editing").find(".edit").setValue("").pressEnter();
        $$("#todo-list>li").shouldHave(exactTexts("c", "d"));

        //toggle(complete) all
        $("#toggle-all").click();
        $$("#todo-list>li").filterBy(cssClass("completed")).shouldHave(exactTexts("c", "d"));

        //(optional) counter = 0
        $("#todo-count>*").shouldHave(exactText("0"));

        //clear all completed
        $("#clear-completed").click();
        $("#todo-list").shouldNotBe(visible);
    }

}
