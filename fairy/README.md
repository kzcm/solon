
# Fairy demo


#### Rpc接口申明

```java
@FairyClient("test:/ComplexModelService/")
public interface IComplexModelService {
    //持久化
    void save(ComplexModel model);

    //读取
    ComplexModel read(Integer modelId);
}
```


#### 使用示例1

```java
@XBean
public class Demo1{
    //
    // 直接注入，需要框架适配
    //
    @XInject
    IComplexModelService service;
    
    public void test(){
        ComplexModel tmp = service.read(1);
        service.save(tmp);
    }
}
```

#### 使用示例2

```java
@XBean
public class Demo2{
    IComplexModelService service = Fairy.builder()
                                        .encoder(SnackTypeEncoder.instance)
                                        .decoder(HessionDecoder.instance)
                                        .upstream(()->{
                                            return "http://localhost:8080";
                                        }).create(IComplexModelService.class);
    
    public void test(){
        ComplexModel tmp = service.read(1);
        service.save(tmp);
    }
}
```