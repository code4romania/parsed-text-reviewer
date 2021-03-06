package ro.code4.curator.controller;

import java.util.List;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ro.code4.curator.entity.ParsedTextManager;
import ro.code4.curator.transferObjects.ParsedTextTO;

@Controller
@RequestMapping("/input/parsed")
public class ParsedTextController {

    @Autowired
    private ParsedTextManager parsedInputService;

    @ApiOperation(value = "createOrUpdate")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ParsedTextTO acceptParsedText(@RequestBody ParsedTextTO input) {
        return parsedInputService.submitParsedText(input);
    }

    @ApiOperation(value = "getAll")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<ParsedTextTO> getAll() {
        return parsedInputService.getAllParsedTexts();
    }

    @ApiOperation(value = "getById")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ParsedTextTO getById(@PathVariable int id) {
        return parsedInputService.getParsedTextById(id);
    }

    @ApiOperation(value = "deleteById")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    void deleteById(@PathVariable int id) {
        parsedInputService.deleteParsedTextById(id);
    }
}
