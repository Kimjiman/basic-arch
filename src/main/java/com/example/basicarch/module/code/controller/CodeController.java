package com.example.basicarch.module.code.controller;

import com.example.basicarch.module.code.facade.CodeCacheFacade;
import com.example.basicarch.module.code.facade.CodeFacade;
import com.example.basicarch.module.code.model.CodeGroupModel;
import com.example.basicarch.module.code.model.CodeGroupSearchParam;
import com.example.basicarch.module.code.model.CodeModel;
import com.example.basicarch.module.code.model.CodeSearchParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/code")
public class CodeController {
    private final CodeCacheFacade codeCacheFacade;
    private final CodeFacade codeFacade;

    @GetMapping("/codeGroup")
    public List<CodeGroupModel> selectCodeGroupList(CodeGroupSearchParam param) {
        return codeCacheFacade.findCodeGroupAllBy(param);
    }

    @GetMapping("/code")
    public List<CodeModel> selectCodeList(CodeSearchParam param) {
        return codeCacheFacade.findCodeAllBy(param);
    }

    @GetMapping("/codeGroup/{id}")
    public CodeGroupModel selectCodeGroupById(@PathVariable Long id) {
        return codeCacheFacade.findCodeGroupById(id);
    }

    @GetMapping("/code/{id}")
    public CodeModel selectCodeById(@PathVariable Long id) {
        return codeCacheFacade.findCodeById(id);
    }

    @PostMapping("/codeGroup")
    public void createCodeGroup(@RequestBody CodeGroupModel codeGroupModel) {
        codeFacade.createCodeGroup(codeGroupModel);
    }

    @PostMapping("/code")
    public void createCode(@RequestBody CodeModel codeModel) {
        codeFacade.createCode(codeModel);
    }

    @PutMapping("/codeGroup")
    public void updateCodeGroup(@RequestBody CodeGroupModel codeGroupModel) {
        codeFacade.updateCodeGroup(codeGroupModel);
    }

    @PutMapping("/code")
    public void updateCode(@RequestBody CodeModel codeModel) {
        codeFacade.updateCode(codeModel);
    }

    @DeleteMapping("/codeGroup/{id}")
    public void deleteCodeGroup(@PathVariable Long id) {
        codeFacade.removeCodeGroupById(id);
    }

    @DeleteMapping("/code/{id}")
    public void deleteCode(@PathVariable Long id) {
        codeFacade.removeCodeById(id);
    }
}
