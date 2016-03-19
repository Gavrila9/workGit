/***************************************************************************************************
 * @Title: TestParamHandler.java                                                                             *
 * @Date: 2015年8月3日 下午5:01:46                                                                 *
 * @Since: JDK 1.7                                                                               *
 * @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved     *
 * *********************************************************************************************** *
 * 注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发													   *
 ***************************************************************************************************/
package com.gongfutrip.service.api.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.gongfutrip.api.web.pricing.RandomOD;
import com.gongfutrip.codec.Base64_16;
import com.gongfutrip.service.api.core.constant.ApiConstant;
import com.gongfutrip.service.api.dto.Authentication;
import com.gongfutrip.service.api.dto.BookingDTO;
import com.gongfutrip.service.api.dto.CancelDTO;
import com.gongfutrip.service.api.dto.PassengerDTO;
import com.gongfutrip.service.api.dto.PreciseBookingDTO;
import com.gongfutrip.service.api.dto.PrecisePricingDTO;
import com.gongfutrip.service.api.dto.PricingDTO;
import com.gongfutrip.service.api.dto.SearchAirLegDTO;
import com.gongfutrip.service.api.dto.SearchSolutionDTO;
import com.gongfutrip.service.api.dto.SegmentDTO;
import com.gongfutrip.service.api.dto.SolutionDTO;
import com.gongfutrip.service.api.model.Flight;
import com.gongfutrip.service.api.model.PricingResponse;
import com.gongfutrip.service.api.model.Response;
import com.gongfutrip.service.api.model.Segment;
import com.gongfutrip.service.api.model.Solution;

/**
 * @author Jango
 * @ClassName: TestParamHandler
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015年8月3日 下午5:01:46
 * @since JDK 1.7
 */
public class TestParamHandler {

	public static Authentication getAuth() {
		Authentication auth = new Authentication();

		auth.setPartnerId("RksVSX7PfZm1yF04adBWYsCD7M4=");
		String sign = DigestUtils.md5Hex(auth.getPartnerId().concat("NjU2OTJiMjAwMTMzY2RkOTg4OWMyY2NkNTg4ODRlOTg="));
		auth.setSign(sign);
		return auth;
	}

	public static void main(String[] args) {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("authentication", getAuth());
		params.put("flightIds", "1f220fdbd19c6815ab2b532f304ce5da");
		param.put("param", params);
		System.out.println(Base64_16.b64encode(JSON.toJSONBytes(params)));
	}

	public static SearchSolutionDTO getShoppingDTO() {
		SearchSolutionDTO shoppingDTO = new SearchSolutionDTO();
		shoppingDTO.setAdults(1);
		// shoppingDTO.setChildren(0);
		// shoppingDTO.setInfants(0);
		shoppingDTO.setAirline("");
		shoppingDTO.setNonstop(0);
		// shoppingDTO.setSolutions(20);

		SearchAirLegDTO airLeg = new SearchAirLegDTO();
		List<SearchAirLegDTO> searchAirLegs = new LinkedList<SearchAirLegDTO>();

		airLeg.setCabinClass("Economy");
		airLeg.setDepartureDate("2016-05-02");
		airLeg.setOrigin("HKG");
		airLeg.setDestination("SYD");

		searchAirLegs.add(airLeg);

		/*
		 * SearchAirLegDTO airLeg2 = new SearchAirLegDTO();
		 * airLeg2.setCabinClass("Economy");
		 * airLeg2.setDepartureDate("2016-05-3"); airLeg2.setOrigin("SYD");
		 * airLeg2.setDestination("HKG");
		 */

		// searchAirLegs.add(airLeg2);

		shoppingDTO.setSearchAirLegs(searchAirLegs);
		shoppingDTO.setSolutions(20);
		return shoppingDTO;
	}

	public static SearchSolutionDTO getRandomShoppingDTO() {
		SearchSolutionDTO shoppingDTO = new SearchSolutionDTO();
		shoppingDTO.setAdults(1);
		shoppingDTO.setChildren(0);
		shoppingDTO.setInfants(0);
		shoppingDTO.setAirline("");
		shoppingDTO.setNonstop(0);
		shoppingDTO.setSolutions(20);

		SearchAirLegDTO airLeg = new SearchAirLegDTO();
		List<SearchAirLegDTO> searchAirLegs = new LinkedList<SearchAirLegDTO>();
		String[] od = RandomOD.getRandomOD().split(",");
		airLeg.setCabinClass("Y");
		Calendar calendar = Calendar.getInstance();
		airLeg.setDepartureDate(RandomOD.getRandomDate(calendar));
		airLeg.setOrigin(od[0]);
		airLeg.setDestination(od[1]);
		searchAirLegs.add(airLeg);
		SearchAirLegDTO airLeg2 = new SearchAirLegDTO();

		airLeg2.setCabinClass("Y");
		airLeg2.setDepartureDate(RandomOD.getRandomDate(calendar));
		airLeg2.setOrigin(od[1]);
		airLeg2.setDestination(od[0]);

		searchAirLegs.add(airLeg2);

		shoppingDTO.setSearchAirLegs(searchAirLegs);
		shoppingDTO.setSolutions(20);
		return shoppingDTO;
	}

	public static PricingDTO getPricingDTO(Response shoppingResponse, Solution solution, int adt) {
		PricingDTO pricingDTO = new PricingDTO();
		String solutionId = solution.getSolutionId();
		Map<String, List<String>> journeys = solution.getJourneys();
		int size = journeys.size();
		List<String> flightIds = new ArrayList<String>(size);
		for (int i = 0; i < size; i++) {
			String flightId = journeys.get(ApiConstant.JOURNEY_PREFIX.concat(String.valueOf(i))).get(0);
			flightIds.add(flightId);
		}
		pricingDTO.setAdults(adt);
		pricingDTO.setSolutionId(solutionId);
		pricingDTO.setFlightIds(flightIds);
		return pricingDTO;
	}

	public static BookingDTO getBookingDTO() throws UnsupportedEncodingException {
		BookingDTO bookingDTO = new BookingDTO();

		List<PassengerDTO> psgs = new ArrayList<PassengerDTO>();
		bookingDTO.setPassengers(psgs);

		PassengerDTO psg_0 = new PassengerDTO();
		psg_0.setBirthday("1975-09-27");
		psg_0.setCardExpiredDate("2024-12-16");
		psg_0.setCardNum("E4418978932");
		psg_0.setCardType("GA");
		psg_0.setFirstName("KIKI");
		psg_0.setLastName("LI");
		psg_0.setNationality("CN");
		psg_0.setPsgType("ADT");
		psg_0.setSex("Male");

		psgs.add(psg_0);
		// psgs.add(psg_1);

		Map<String, String> journeys = new HashMap<String, String>();
		bookingDTO.setJourneys(journeys);
		journeys.put(ApiConstant.JOURNEY_PREFIX.concat("0"), "bdbc58154a105a9e20595ac055e61b89");
		// journeys.put(ApiConstant.JOURNEY_PREFIX.concat("1"),
		// "a7b67c6f43207352e2873085b68de02f");

		bookingDTO.setSolutionId("aafcb7c0fdfc2432da8d2da210e17a90");
		return bookingDTO;
	}

	public static CancelDTO getCancelDTO() throws UnsupportedEncodingException {
		CancelDTO cancelDTO = new CancelDTO();
		cancelDTO.setOrderNum(1012666145L);
		cancelDTO.setPnr(StringUtils.reverse("PP9VD6"));
		return cancelDTO;
	}

	public static PrecisePricingDTO getPrecisePricingDTO() throws Exception {
		PrecisePricingDTO precisePricingDTO = new PrecisePricingDTO();
		precisePricingDTO.setAdults(1);
		precisePricingDTO.setChildren(0);
		precisePricingDTO.setInfants(0);

		Map<String, List<SegmentDTO>> map = new HashMap<String, List<SegmentDTO>>();

		List<SegmentDTO> segments0 = new ArrayList<SegmentDTO>();
		SegmentDTO SegmentDTO0 = new SegmentDTO();
		SegmentDTO0.setAirline("CZ");
		SegmentDTO0.setFlightNum("3147");
		SegmentDTO0.setDeparture("BHY");
		SegmentDTO0.setArrival("CSX");
		SegmentDTO0.setBookingCode("Q");
		SegmentDTO0.setDepartureDate("2015-09-19");
		SegmentDTO0.setDepartureTime("08:05");
		SegmentDTO0.setArrivalDate("2015-09-19");
		SegmentDTO0.setArrivalTime("09:35");
		segments0.add(SegmentDTO0);

		SegmentDTO SegmentDTO1 = new SegmentDTO();
		SegmentDTO1.setAirline("CZ");
		SegmentDTO1.setFlightNum("3065");
		SegmentDTO1.setDeparture("CSX");
		SegmentDTO1.setArrival("ICN");
		SegmentDTO1.setBookingCode("Q");
		SegmentDTO1.setDepartureDate("2015-09-19");
		SegmentDTO1.setDepartureTime("12:15");
		SegmentDTO1.setArrivalDate("2015-09-19");
		SegmentDTO1.setArrivalTime("16:00");
		segments0.add(SegmentDTO1);
		map.put(ApiConstant.JOURNEY_PREFIX.concat("0"), segments0);

		// List<SegmentDTO> segments1 = new ArrayList<SegmentDTO>();
		// SegmentDTO SegmentDTO1 = new SegmentDTO();
		// SegmentDTO1.setAirline("SA");
		// SegmentDTO1.setFlightNum("303");
		// SegmentDTO1.setDeparture("JNB");
		// SegmentDTO1.setArrival("CPT");
		// SegmentDTO1.setBookingCode("N");
		// SegmentDTO1.setDepartureDate("2015-11-25");
		// SegmentDTO1.setDepartureTime("06:00");
		// SegmentDTO1.setArrivalDate("2015-11-25");
		// SegmentDTO1.setArrivalTime("08:10");
		// segments1.add(SegmentDTO1);
		// map.put(ApiConstant.JOURNEY_PREFIX.concat("1"), segments1);
		precisePricingDTO.setJourneys(map);
		return precisePricingDTO;
	}

	/**
	 * @param pricingRes
	 * @return
	 * @Title: getBookingDTO
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 */
	public static BookingDTO getBookingDTO(PricingResponse pricingRes) {

		BookingDTO bookingDTO = new BookingDTO();

		List<PassengerDTO> psgs = new ArrayList<PassengerDTO>();
		bookingDTO.setPassengers(psgs);

		PassengerDTO psg_0 = new PassengerDTO();
		psg_0.setBirthday("2009-10-22");
		psg_0.setCardExpiredDate("2020-02-05");
		psg_0.setCardNum("1112365896223");
		psg_0.setCardType("PASSPORT");
		psg_0.setFirstName("JangoO");
		psg_0.setLastName("Wong");
		psg_0.setNationality("CN");
		psg_0.setPsgType("ADT");
		psg_0.setSex("Male");

		psgs.add(psg_0);
		PassengerDTO psg_1 = new PassengerDTO();
		psg_1.setBirthday("2009-10-22");
		psg_1.setCardExpiredDate("2020-02-05");
		psg_1.setCardNum("G12345678");
		psg_1.setCardType("PASSPORT");
		psg_1.setFirstName("Pogy");
		psg_1.setLastName("Yao");
		psg_1.setNationality("CN");
		psg_1.setPsgType("ADT");
		psg_1.setSex("Male");

		psgs.add(psg_1);

		Map<String, List<String>> originJourneys = pricingRes.getSolution().getJourneys();
		Map<String, String> journeys = new HashMap<String, String>();
		for (Entry<String, List<String>> entry : originJourneys.entrySet()) {
			journeys.put(entry.getKey(), entry.getValue().get(0));
		}
		bookingDTO.setJourneys(journeys);
		bookingDTO.setSolutionId(pricingRes.getSolution().getSolutionId());
		return bookingDTO;
	}

	/**
	 * @param solution
	 * @param flights
	 * @return
	 * @Title: getBookingDTO
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 */
	public static PreciseBookingDTO getPreciseBookingDTO(Solution solution, Map<String, Flight> flights) {

		PreciseBookingDTO bookingDTO = new PreciseBookingDTO();

		SolutionDTO solutionDTO = new SolutionDTO();
		solutionDTO.setAdtFare(solution.getAdtFare());
		solutionDTO.setAdtTax(solution.getAdtTax());
		Map<String, List<SegmentDTO>> journeys = new HashMap<String, List<SegmentDTO>>();
		solutionDTO.setJourneys(journeys);
		bookingDTO.setSolution(solutionDTO);

		for (Entry<String, Flight> entry : flights.entrySet()) {
			String key = entry.getKey();
			Flight flight = entry.getValue();
			List<SegmentDTO> dtos = new LinkedList<SegmentDTO>();
			List<Segment> segs = flight.getSegments();
			for (Segment segment : segs) {
				SegmentDTO dto = new SegmentDTO();
				dto.setAirline(segment.getAirline());
				dto.setArrival(segment.getArrival());
				dto.setArrivalDate(segment.getStrArrivalDate());
				dto.setArrivalTime(segment.getStrArrivalTime());
				dto.setBookingCode(segment.getBookingCode());
				dto.setDeparture(segment.getDeparture());
				dto.setDepartureDate(segment.getStrDepartureDate());
				dto.setDepartureTime(segment.getStrDepartureTime());
				dto.setFlightNum(segment.getFlightNum());
				dtos.add(dto);
			}
			journeys.put(key, dtos);
		}

		List<PassengerDTO> psgs = new ArrayList<PassengerDTO>();
		bookingDTO.setPassengers(psgs);
		PassengerDTO psg_0 = new PassengerDTO();
		psg_0.setBirthday("2009-10-22");
		psg_0.setCardExpiredDate("2020-02-05");
		psg_0.setCardNum("G123456987");
		psg_0.setCardType("P");
		psg_0.setFirstName("Jango");
		psg_0.setLastName("Wong");
		psg_0.setNationality("CN");
		psg_0.setPsgType("ADT");
		psg_0.setSex("M");
		psgs.add(psg_0);

		PassengerDTO psg_1 = new PassengerDTO();
		psg_1.setBirthday("2009-10-22");
		psg_1.setCardExpiredDate("2020-02-05");
		psg_1.setCardNum("G123456981");
		psg_1.setCardType("PASSPORT");
		psg_1.setFirstName("Pogy");
		psg_1.setLastName("Yao");
		psg_1.setNationality("CN");
		psg_1.setPsgType("ADT");
		psg_1.setSex("Male");
		// psgs.add(psg_1);
		return bookingDTO;
	}

	/**
	 * @param flights
	 * @return
	 * @Title: getPrecisePricingDTO
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 */
	public static PrecisePricingDTO getPrecisePricingDTO(Solution solution, Map<String, Flight> flights) {
		PrecisePricingDTO precisePricingDTO = new PrecisePricingDTO();
		precisePricingDTO.setAdults(1);
		precisePricingDTO.setChildren(0);
		precisePricingDTO.setInfants(0);

		Map<String, List<SegmentDTO>> map = new HashMap<String, List<SegmentDTO>>();

		for (Entry<String, Flight> entry : flights.entrySet()) {
			String key = entry.getKey();
			Flight flight = entry.getValue();
			List<SegmentDTO> dtos = new ArrayList<SegmentDTO>();

			List<Segment> segs = flight.getSegments();
			for (Segment segment : segs) {
				SegmentDTO dto = new SegmentDTO();
				dto.setAirline(segment.getAirline());
				dto.setArrival(segment.getArrival());
				dto.setArrivalDate(segment.getStrArrivalDate());
				dto.setArrivalTime(segment.getStrArrivalTime());
				dto.setBookingCode(segment.getBookingCode());
				dto.setDeparture(segment.getDeparture());
				dto.setDepartureDate(segment.getStrDepartureDate());
				dto.setDepartureTime(segment.getStrDepartureTime());
				dto.setFlightNum(segment.getFlightNum());
				dtos.add(dto);
			}
			map.put(key, dtos);
		}
		precisePricingDTO.setJourneys(map);
		return precisePricingDTO;
	}

	/**
	 * @return
	 * @Title: getTianTaiAuth
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 */
	public static Authentication getTianTaiAuth() {
		Authentication auth = new Authentication();
		auth.setPartnerId("U8CVxHenYQ1R9Rs897u8h6gP6rQ=");
		String sign = DigestUtils.md5Hex(auth.getPartnerId().concat("NGQ0NzkxMDUwZDNjMzU0YjFhNjk3Mjk5NWYxMjQzZTM="));
		auth.setSign(sign);
		return auth;
	}
}
