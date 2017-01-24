package cn.aiyuedu.bs.dao.util;

import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.common.model.CategoryBase;
import cn.aiyuedu.bs.dao.dto.FilterWordDto;
import cn.aiyuedu.bs.dao.entity.Book;
import cn.aiyuedu.bs.dao.entity.Chapter;
import cn.aiyuedu.bs.dao.dto.BookDto;
import cn.aiyuedu.bs.dao.dto.CategoryDto;
import cn.aiyuedu.bs.dao.dto.ChapterDto;
import cn.aiyuedu.bs.dao.entity.Category;
import cn.aiyuedu.bs.dao.entity.FilterWord;
import org.springframework.cglib.beans.BeanCopier;

/**
 * Description:
 *
 * @author yz.wu
 */
public class BeanCopierUtils {

    private static BeanCopier bookCopier = BeanCopier.create(Book.class, BookDto.class, false);
    private static BeanCopier bookBaseCopier = BeanCopier.create(Book.class, BookBase.class, false);
    private static BeanCopier chapterCopier = BeanCopier.create(Chapter.class, ChapterDto.class, false);
    private static BeanCopier categoryCopier = BeanCopier.create(Category.class, CategoryDto.class, false);
    private static BeanCopier categoryBaseCopier = BeanCopier.create(Category.class, CategoryBase.class, false);
    private static BeanCopier filterWordCopier = BeanCopier.create(FilterWord.class, FilterWordDto.class, false);

    public static void bookCopy(Book book, BookDto bookDto) {
        bookCopier.copy(book, bookDto, null);
    }

    public static void bookBaseCopy(Book book, BookBase bookBase) {
        bookBaseCopier.copy(book, bookBase, null);
    }

    public static void chapterCopy(Chapter chapter, ChapterDto chapterDto) {
        chapterCopier.copy(chapter, chapterDto, null);
    }

    public static void categoryCopy(Category category, CategoryDto categoryDto) {
        categoryCopier.copy(category, categoryDto, null);
    }

    public static void categoryBaseCopy(Category category, CategoryBase categoryBase) {
        categoryBaseCopier.copy(category, categoryBase, null);
    }

    public static void filterWordCopy(FilterWord filterWord, FilterWordDto filterWordDto) {
        filterWordCopier.copy(filterWord, filterWordDto, null);
    }
}
