package com.example.controller;

import com.example.common.Result;
import com.example.entity.Employee;
import com.example.service.EmployeeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    /**
     * 新增数据
     */
    @PostMapping("/add")
    public Result add(@RequestBody Employee employee) {
        employeeService.add(employee);
        return Result.success();
    }

    /**
     * 更新数据
     */
    @PutMapping("/update")
    public Result update(@RequestBody Employee employee) {
        employeeService.update(employee);
        return Result.success();
    }

    /**
     *删除单个数据
     */
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        employeeService.deleteById(id);
        return Result.success();
    }

    /**
     * 查询单个的数据
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    /**
     * 查询所有的数据
     * @return
     */
    @GetMapping("/selectAll")
    public Result selectAll(Employee employee){
        List<Employee> list = employeeService.selectAll(employee);
        return Result.success(list);
    }

    /**
     * 分页查询的数据
     * pageNum：当前的页码
     * pageSize：每页的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(Employee employee,
                             @RequestParam(defaultValue = "1")Integer pageNum,
                             @RequestParam(defaultValue = "10")Integer pageSize){
        PageInfo<Employee> pageInfo = employeeService.selectPage(employee,pageNum,pageSize);
        return Result.success(pageInfo);
    }

}
