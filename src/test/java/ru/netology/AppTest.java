package ru.netology;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static ru.netology.entity.Country.RUSSIA;
import static ru.netology.entity.Country.USA;
import static ru.netology.geo.GeoServiceImpl.MOSCOW_IP;
import static ru.netology.geo.GeoServiceImpl.NEW_YORK_IP;

public class AppTest {

    @Test
    public void message_sender_russia_test() {
        GeoServiceImpl geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(MOSCOW_IP))
                .thenReturn(new Location("Moscow", RUSSIA, "Lenina", 15));

        LocalizationServiceImpl localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(RUSSIA)).thenReturn("Добро пожаловать");

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.0.32.11");

        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);

        String expected = "Добро пожаловать";
        String actual = messageSender.send(headers);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void message_sender_usa_test(){
        GeoServiceImpl geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(NEW_YORK_IP))
                .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));

        LocalizationServiceImpl localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(USA)).thenReturn("Welcome");

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.44.183.149");

        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);

        String expected = "Welcome";
        String actual = messageSender.send(headers);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void location_test_by_ip(){
        GeoServiceImpl geoService = new GeoServiceImpl();
        Location actual1 = geoService.byIp(MOSCOW_IP);
        Location actual2 = geoService.byIp(NEW_YORK_IP);
        Location actual3 = geoService.byIp("172.0.10.10");

        Location expected1 = new Location("Moscow", Country.RUSSIA, "Lenina", 15);
        Location expected2 = new Location("New York", Country.USA, " 10th Avenue", 32);
        Location expected3 = new Location("Moscow", Country.RUSSIA, null, 0);

        Assert.assertEquals(actual1, expected1);
        Assert.assertEquals(actual2, expected2);
        Assert.assertEquals(actual3, expected3);
    }

    @Test
    public void locale_test(){
        LocalizationServiceImpl localizationService = new LocalizationServiceImpl();
        String expected1 = "Добро пожаловать";
        String expected2 = "Welcome";

        String actual1 = localizationService.locale(RUSSIA);
        String actual2 = localizationService.locale(USA);

        Assert.assertEquals(expected1, actual1);
        Assert.assertEquals(expected2, actual2);

    }
}
