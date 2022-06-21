package com.example.dbcontrollerapi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexingController {

    @PostMapping("add-index/{db}/{collection}")
    public void addIndex(){
        // update collection json file
        // notify RORs
    }

    @PostMapping("delete-index/{db}/{collection}")
    public void deleteIndex(){
        // update collection json file
        // notify RORs
    }

}
