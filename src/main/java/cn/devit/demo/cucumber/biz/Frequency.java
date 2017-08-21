package cn.devit.demo.cucumber.biz;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNull;

/**
 * 班期周期的字符串的对象表示。
 * <p>
 * 接受一个仅包含“1234567”的字符串，转换成方便操作的对象。
 *
 *
 * @author lxb
 *
 */
public final class Frequency implements Serializable {

    /**
     * 班期为空
     */
    public static final Frequency EMPTY = new Frequency();

    public static final Frequency FULL = new Frequency("1234567");

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @NonNull
    private final TreeSet<Integer> set = new TreeSet<Integer>();

    /**
     * 构造一个空的班期
     */
    private Frequency() {
    }

    // TODO 从一个日期集合中构造班期。
    // TODO 计算两个班期的offset，要求班期的数量相同，间距相同，这样计算结果才能相同
    // TODO merged 操作。合并两个班期

    /**
     * 按照参数构造一个班期对象， <code>null</code>和空字符串构造一个空的班期。
     * 
     * @param dayOfWeek
     *            仅包含1-7的字符串,另外还有两种速记情况：一个D表示1234567，X开头则表示是相反的，X1表示234567。
     * @throws IllegalArgumentException 入参有不合规的字符
     *
     */
    public Frequency(final String dayOfWeek) {
        if (dayOfWeek == null || dayOfWeek.isEmpty()) {
            return;// 空的班期
        }
        String temp = dayOfWeek.toUpperCase();
        // 处理速记符
        char head = temp.charAt(0);
        if (dayOfWeek.length() == 1 && head == 'D') {
            this.set.addAll(FULL.set);
            return;// 全集
        }
        if (head == 'X') {
            this.set.addAll(FULL.set);
            this.set.removeAll(new Frequency(temp.substring(1)).set);
            return;
        }
        for (int i = 0; i < temp.length(); i++) {
            char o = temp.charAt(i);
            if ('1' <= o && o <= '7') {
                set.add(o - 48);
            } else {
                throw new IllegalArgumentException("班期字符串包含不能解析的字符：" + o);
            }

        }
        // org.joda.time.DateTimeConstants
        // 或者 java8 DayOfWeek
    }

    /**
     * @param used
     *            使用设置好的集合初始化班期。
     *
     * @throws IllegalArgumentException
     *             集合有[1,7]之外的数字。
     */
    Frequency(Set<Integer> used) {
        if (used == null) {
            return;
        }
        for (Integer i : used) {
            if (i < 1 || 7 < i) {
                throw new IllegalArgumentException("班期包含其他字符：" + i);
            }
            set.add(i);
        }
    }

    /**
     * 是否包含一个星期（1-7）
     *
     * @param dayOfWeek 取值：[1,7]
     * @return 是:true
     * @see {@link org.joda.time.DateTimeConstants#MONDAY}.
     */
    boolean contains(Integer dayOfWeek) {
        return set.contains(dayOfWeek);
    }

    public static Frequency valueOf(String f) {
        return new Frequency(f);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((set == null) ? 0 : set.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Frequency other = (Frequency) obj;
        return set.equals(other.set);
    }

    /**
     * 输出字符串形式的班期。
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer c : set) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 进行日期偏移操作，返回<font color="red">新</font>的对象。
     * <p>
     *
     * <pre>
     * "12"+1="23"
     * "17"-1="67"
     * </pre>
     *
     * @param day
     *            偏移的日期正数是右移，负数是左移。
     * @return 新的班期
     */
    public Frequency shift(int day) {
        if (day % 7 == 0) {
            return this;
        }
        Frequency f = new Frequency();
        for (Integer week : this.set) {
            week += day;
            if (week > 0) {
                week = (week - 1) % 7 + 1;
            } else {
                week = 7 + (week) % 7;
            }
            f.set.add(week);
        }
        return f;
    }

    /**
     * @return 星期数字的集合，只读视图。
     */
    public SortedSet<Integer> set() {
        return Collections.unmodifiableSortedSet(this.set);
    }

    /**
     * 两个班期是否有冲突（有交集）。
     *
     * @param other
     *            另一个班期
     * @return true：有；false，没有。
     */
    public boolean conflict(Frequency other) {
        if (other == null) {
            throw new IllegalArgumentException("other frequency == null.");
        }
        return !Collections.disjoint(this.set, other.set);
    }

    /**
     * This 和 other的差集。this中刨除与other相同部分后的剩余。
     *
     * @param other
     * @return 新的集合，集合中的数字是this中去除和other交集后的剩余。
     */
    public Frequency difference(Frequency other) {
        @SuppressWarnings("unchecked")
        TreeSet<Integer> clone = (TreeSet<Integer>) this.set.clone();
        clone.removeAll(other.set);
        return new Frequency(clone);
    }

    /**
     * This 是否全包含 frequency
     *
     * @param frequency
     * @return <code>true</code> if this ⊇ frequency，else false。
     */
    public boolean containsAll(Frequency frequency) {
        return this.set.containsAll(frequency.set);
    }

}