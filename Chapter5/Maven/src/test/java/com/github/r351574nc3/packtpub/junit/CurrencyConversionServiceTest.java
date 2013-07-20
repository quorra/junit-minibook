package com.github.r351574nc3.packtpub.junit;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author Leo Przybylski
 */
@RunWith(JUnit4.class)
public class CurrencyConversionServiceTest {
    protected CurrencyConversionService usdToGbpConversionService;

    @Before
    public void setupConversionService() {
        usdToGbpConversionService = new CurrencyConversionServiceImpl();

        try {
            final Method setSourceMethod  = usdToGbpConversionService.getClass()
                .getDeclaredMethod("setSourceCurrency", CurrencyService.class);
            setSourceMethod.setAccessible(true); // Ignore Private scope
            setSourceMethod.invoke(usdToGbpConversionService, new CurrencyService() {
                    public BigDecimal getConversionRatio() {
                        return new BigDecimal(1.0);
                    }
                });

        }
        catch (Exception e) {
        }

        try {
            final Method setTargetMethod = usdToGbpConversionService.getClass()
                .getDeclaredMethod("setTargetCurrency", CurrencyService.class);
            setTargetMethod.setAccessible(true); // Ignore Private scope
            setTargetMethod.invoke(usdToGbpConversionService, new CurrencyService() {
                    public BigDecimal getConversionRatio() {
                        return new BigDecimal(0.66);
                    }
                });

        }
        catch (Exception e) {
        }
    }

    @Test
    public void testConvertUsdToGbp() {
        final BigDecimal result = usdToGbpConversionService.convert(new BigDecimal(100.0));
        assertEquals("Conversion inaccurate", result, new BigDecimal(66.0));
    }

}