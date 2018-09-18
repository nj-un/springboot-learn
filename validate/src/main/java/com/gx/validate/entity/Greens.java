package com.gx.validate.entity;

import com.gx.validate.groups.Groups;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author: gaoxu
 * @date: 2018/9/18
 */
public class Greens {

    @NotNull(message = "id 不能为空", groups = Groups.Update.class)
    private Integer id;

    @NotBlank(message = "name 不允许为空", groups = Groups.Default.class)
    private String name;

    @NotNull(message = "price 不允许为空", groups = Groups.Default.class)
    private BigDecimal price;

    public Greens() {
    }

    public Greens(Integer id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
